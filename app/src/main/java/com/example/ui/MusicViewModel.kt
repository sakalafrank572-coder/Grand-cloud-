package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MusicViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository = MusicPlatformRepository(database.musicPlatformDao())

    // --- AUTHENTICATION STATE ---
    private val _currentUser = MutableStateFlow<ArtistProfile?>(null)
    val currentUser: StateFlow<ArtistProfile?> = _currentUser.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    // --- ACTIVE MUSIC PLAYER STATE ---
    private val _currentPlayingSong = MutableStateFlow<Song?>(null)
    val currentPlayingSong: StateFlow<Song?> = _currentPlayingSong.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _playbackPositionSec = MutableStateFlow(0)
    val playbackPositionSec: StateFlow<Int> = _playbackPositionSec.asStateFlow()

    private val _playbackDurationSec = MutableStateFlow(180)
    val playbackDurationSec: StateFlow<Int> = _playbackDurationSec.asStateFlow()

    // Current playlist queue
    private val _playbackQueue = MutableStateFlow<List<Song>>(emptyList())
    val playbackQueue: StateFlow<List<Song>> = _playbackQueue.asStateFlow()

    // --- SEARCH QUERY ---
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // --- SOCIAL & MESSAGE INPUTS ---
    private val _activeChatRecipient = MutableStateFlow<ArtistProfile?>(null)
    val activeChatRecipient: StateFlow<ArtistProfile?> = _activeChatRecipient.asStateFlow()

    // --- FLOW STREAMS FROM DATABASE ---
    val allSongs: StateFlow<List<Song>> = repository.allSongs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allArtists: StateFlow<List<ArtistProfile>> = repository.allArtists
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBeats: StateFlow<List<BeatListing>> = repository.allBeats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allStatuses: StateFlow<List<Status>> = repository.allStatuses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allReels: StateFlow<List<Reel>> = repository.allReels
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search results flow dynamically combined with searchQuery
    val searchResults: StateFlow<List<Song>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                repository.allSongs
            } else {
                repository.searchSongs(query)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Notifications flow reacting to active user
    val notifications: StateFlow<List<Notification>> = _currentUser
        .flatMapLatest { user ->
            if (user == null) flowOf(emptyList())
            else repository.getNotificationsForUser(user.id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active direct messages flow
    val activeChatMessages: StateFlow<List<Message>> = combine(_currentUser, _activeChatRecipient) { user, recipient ->
        Pair(user, recipient)
    }
    .flatMapLatest { (user, recipient) ->
        if (user == null || recipient == null) flowOf(emptyList())
        else repository.getMessagesBetween(user.id, recipient.id)
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active liked songs
    val likedSongs: StateFlow<List<Song>> = _currentUser
        .flatMapLatest { user ->
            if (user == null) flowOf(emptyList())
            else repository.getLikedSongs(user.id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- AUDIO CUSTOMIZATION ENGINE ---
    private val _autotuneStrength = MutableStateFlow(0f) // 0f (Off) to 1f (Full)
    val autotuneStrength: StateFlow<Float> = _autotuneStrength.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f) // 0.5f to 2.0f
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    private val _bassBoost = MutableStateFlow(0f) // 0f to 1f (0% to 100%)
    val bassBoost: StateFlow<Float> = _bassBoost.asStateFlow()

    fun setAutotuneStrength(strength: Float) {
        _autotuneStrength.value = strength.coerceIn(0f, 1f)
    }

    fun setPlaybackSpeed(speed: Float) {
        _playbackSpeed.value = speed.coerceIn(0.5f, 2.0f)
        startPlayerTimer()
    }

    fun setBassBoost(bass: Float) {
        _bassBoost.value = bass.coerceIn(0f, 1f)
    }

    fun resetAudioEffects() {
        _autotuneStrength.value = 0f
        _playbackSpeed.value = 1.0f
        _bassBoost.value = 0f
        startPlayerTimer()
    }

    // --- SIMULATED PLAYER TIMER JOB ---
    private var playerTimerJob: Job? = null

    init {
        // Start simulated music player timer loop
        startPlayerTimer()
    }

    private fun startPlayerTimer() {
        playerTimerJob?.cancel()
        playerTimerJob = viewModelScope.launch {
            while (true) {
                val currentSpeed = _playbackSpeed.value
                val tickDelay = (1000f / currentSpeed).toLong().coerceAtLeast(100L)
                delay(tickDelay)
                if (_isPlaying.value && _currentPlayingSong.value != null) {
                    val nextSec = _playbackPositionSec.value + 1
                    if (nextSec >= _playbackDurationSec.value) {
                        // Song ended, auto next or loop
                        skipToNext()
                    } else {
                        _playbackPositionSec.value = nextSec
                    }
                }
            }
        }
    }

    // --- AUTH ACTIONS ---
    fun login(username: String, passHex: String, onSuccess: () -> Unit) {
        val cleanUser = username.trim().lowercase()
        if (cleanUser.isEmpty()) {
            _authError.value = "Username cannot be empty."
            return
        }

        viewModelScope.launch {
            val user = repository.getArtistByIdDirect(cleanUser)
            if (user != null) {
                if (user.password == passHex) {
                    _currentUser.value = user
                    _authError.value = null
                    onSuccess()
                } else {
                    _authError.value = "Incorrect password."
                }
            } else {
                _authError.value = "Account does not exist. Please register first."
            }
        }
    }

    fun register(username: String, email: String, passHex: String, onSuccess: () -> Unit) {
        val cleanUser = username.trim().lowercase()
        if (cleanUser.isEmpty()) {
            _authError.value = "Username cannot be empty."
            return
        }
        val cleanEmail = email.trim()
        if (cleanEmail.isEmpty()) {
            _authError.value = "Email address cannot be empty."
            return
        }

        viewModelScope.launch {
            val existing = repository.getArtistByIdDirect(cleanUser)
            if (existing != null) {
                _authError.value = "Username is already registered."
                return@launch
            }

            // Select a random modern gradient for the user's avatar
            val gradients = listOf(
                Pair("#00FFE0", "#0051FF"),
                Pair("#FF5E62", "#FF9966"),
                Pair("#00FF90", "#00FFE0"),
                Pair("#FF0099", "#493240")
            )
            val randomGradient = gradients.random()

            val newUser = ArtistProfile(
                id = cleanUser,
                displayName = username.trim(),
                email = cleanEmail,
                bio = "Independent artist. Exploring new horizons in Sound.",
                avatarGradientStart = randomGradient.first,
                avatarGradientEnd = randomGradient.second,
                isArtist = true,
                isVerified = false,
                followers = 0,
                following = 0,
                password = passHex
            )
            repository.insertArtist(newUser)
            _currentUser.value = newUser
            _authError.value = null
            onSuccess()
        }
    }

    fun logout() {
        _currentUser.value = null
        _isPlaying.value = false
        _currentPlayingSong.value = null
    }

    // --- PLAYER ACTIONS ---
    fun playSong(song: Song, queue: List<Song> = emptyList()) {
        viewModelScope.launch {
            _currentPlayingSong.value = song
            _playbackDurationSec.value = song.durationSec
            _playbackPositionSec.value = 0
            _isPlaying.value = true
            
            if (queue.isNotEmpty()) {
                _playbackQueue.value = queue
            } else if (!_playbackQueue.value.contains(song)) {
                _playbackQueue.value = _playbackQueue.value + song
            }
            
            // Increment play count of song
            repository.incrementPlayCount(song.id)
        }
    }

    fun togglePlayPause() {
        if (_currentPlayingSong.value != null) {
            _isPlaying.value = !_isPlaying.value
        }
    }

    fun skipToNext() {
        val queue = _playbackQueue.value
        val current = _currentPlayingSong.value
        if (queue.isEmpty() || current == null) return
        
        val currentIndex = queue.indexOfFirst { it.id == current.id }
        if (currentIndex != -1 && currentIndex < queue.size - 1) {
            playSong(queue[currentIndex + 1], queue)
        } else {
            // Loop to beginning of queue
            playSong(queue[0], queue)
        }
    }

    fun skipToPrevious() {
        val queue = _playbackQueue.value
        val current = _currentPlayingSong.value
        if (queue.isEmpty() || current == null) return
        
        val currentIndex = queue.indexOfFirst { it.id == current.id }
        if (currentIndex > 0) {
            playSong(queue[currentIndex - 1], queue)
        } else {
            // Loop to last item
            playSong(queue.last(), queue)
        }
    }

    fun seekToPosition(positionSec: Int) {
        _playbackPositionSec.value = positionSec.coerceIn(0, _playbackDurationSec.value)
    }

    // --- MUSIC ACTIONS ---
    fun uploadSong(title: String, description: String, genre: String, durationSec: Int, startColor: String, endColor: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val newSong = Song(
                title = title,
                artistId = user.id,
                artistName = user.displayName,
                genre = genre,
                description = description,
                durationSec = durationSec,
                coverColorStart = startColor,
                coverColorEnd = endColor
            )
            repository.insertSong(newSong)
        }
    }

    fun updateProfileBio(name: String, bio: String, avatarUri: String? = null) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.updateArtistBioAndAvatar(user.id, bio, name, avatarUri ?: user.avatarUri)
            // Sync local model
            val updated = repository.getArtistByIdDirect(user.id)
            if (updated != null) {
                _currentUser.value = updated
            }
        }
    }

    fun requestVerification() {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.requestVerification(user.id)
            // Sync local model
            val updated = repository.getArtistByIdDirect(user.id)
            if (updated != null) {
                _currentUser.value = updated
            }
        }
    }

    fun search(query: String) {
        _searchQuery.value = query
    }

    // --- COMMENTS ACTIONS ---
    fun postComment(songId: Int, text: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.insertComment(songId, user.id, user.displayName, text)
        }
    }

    fun getSongComments(songId: Int): Flow<List<Comment>> {
        return repository.getCommentsBySong(songId)
    }

    // --- MESSAGES ACTIONS ---
    fun openChatWith(recipient: ArtistProfile) {
        _activeChatRecipient.value = recipient
    }

    fun closeChat() {
        _activeChatRecipient.value = null
    }

    fun sendChatMessage(text: String) {
        val sender = _currentUser.value ?: return
        val receiver = _activeChatRecipient.value ?: return
        if (text.trim().isEmpty()) return
        
        viewModelScope.launch {
            repository.sendMessage(sender.id, sender.displayName, receiver.id, text.trim())
        }
    }

    // --- SOCIAL INTERACTION ACTIONS ---
    fun toggleLikeSong(songId: Int) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.toggleLike(user.id, user.displayName, songId)
        }
    }

    fun toggleRepostSong(songId: Int) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.toggleRepost(user.id, user.displayName, songId)
        }
    }

    fun toggleFollowArtist(artistId: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.toggleFollow(user.id, user.displayName, artistId)
        }
    }

    fun isSongLiked(songId: Int): Flow<Boolean> {
        val user = _currentUser.value ?: return flowOf(false)
        return repository.getSocialAction(user.id, songId).map { it?.isLiked == true }
    }

    fun isSongReposted(songId: Int): Flow<Boolean> {
        val user = _currentUser.value ?: return flowOf(false)
        return repository.getSocialAction(user.id, songId).map { it?.isReposted == true }
    }

    fun isFollowingArtist(artistId: String): Flow<Boolean> {
        val user = _currentUser.value ?: return flowOf(false)
        return repository.getFollowState(user.id, artistId).map { it != null }
    }

    // --- MARKETPLACE ACTIONS ---
    fun uploadBeat(title: String, bpm: Int, price: String, tags: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.insertBeat(BeatListing(
                title = title,
                producerId = user.id,
                producerName = user.displayName,
                price = price,
                bpm = bpm,
                tags = tags
            ))
        }
    }

    // --- REEL & STATUS ENHANCEMENTS ---
    fun getReelsByArtist(artistId: String): Flow<List<Reel>> {
        return repository.getReelsByArtist(artistId)
    }

    fun uploadStatus(
        text: String,
        bgStart: String,
        bgEnd: String,
        videoUri: String? = null,
        imageUri: String? = null,
        linkUrl: String? = null
    ) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.insertStatus(
                Status(
                    artistId = user.id,
                    artistName = user.displayName,
                    text = text,
                    bgGradientStart = bgStart,
                    bgGradientEnd = bgEnd,
                    videoUri = videoUri,
                    imageUri = imageUri,
                    linkUrl = linkUrl
                )
            )
        }
    }

    fun uploadReel(title: String, sound: String, coverStart: String, coverEnd: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.insertReel(
                Reel(
                    artistId = user.id,
                    artistName = user.displayName,
                    title = title,
                    soundName = sound,
                    coverColorStart = coverStart,
                    coverColorEnd = coverEnd
                )
            )
        }
    }

    fun likeReel(reelId: Int) {
        viewModelScope.launch {
            repository.incrementReelLikes(reelId)
        }
    }
}
