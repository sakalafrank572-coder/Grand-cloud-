package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class MusicPlatformRepository(private val dao: MusicPlatformDao) {

    // --- USER PROFILE & AUTHORS ---
    fun getArtistById(id: String): Flow<ArtistProfile?> = dao.getArtistById(id)
    
    suspend fun getArtistByIdDirect(id: String): ArtistProfile? = dao.getArtistByIdDirect(id)
    
    val allArtists: Flow<List<ArtistProfile>> = dao.getAllArtists()

    suspend fun insertArtist(artist: ArtistProfile) = dao.insertArtist(artist)

    suspend fun updateArtistBio(id: String, newBio: String, displayName: String) {
        val current = dao.getArtistByIdDirect(id) ?: return
        dao.updateArtist(current.copy(bio = newBio, displayName = displayName))
    }

    suspend fun updateArtistBioAndAvatar(id: String, newBio: String, displayName: String, avatarUri: String?) {
        val current = dao.getArtistByIdDirect(id) ?: return
        dao.updateArtist(current.copy(bio = newBio, displayName = displayName, avatarUri = avatarUri))
    }

    suspend fun requestVerification(artistId: String) {
        val current = dao.getArtistByIdDirect(id = artistId) ?: return
        dao.updateArtist(current.copy(isVerified = true))
        // Log notification to itself
        dao.insertNotification(Notification(
            userId = artistId,
            title = "Verification Approved 🎉",
            content = "Congratulations! ZM BEATS' automated validator verified your artist account.",
            type = "verify"
        ))
    }


    // --- SONGS ---
    val allSongs: Flow<List<Song>> = dao.getAllSongs()

    fun getSongById(id: Int): Flow<Song?> = dao.getSongById(id)

    suspend fun getSongByIdDirect(id: Int): Song? = dao.getSongByIdDirect(id)

    fun getSongsByArtist(artistId: String): Flow<List<Song>> = dao.getSongsByArtist(artistId)

    suspend fun insertSong(song: Song): Int {
        val songId = dao.insertSong(song).toInt()
        
        // Push notification to followers (mocking this behavior!)
        val artistsList = dao.getArtistByIdDirect(song.artistId)
        if (artistsList != null) {
            dao.insertNotification(Notification(
                userId = "independent_one", // Push to active fan account as a demo
                title = "New Track Upload",
                content = "${song.artistName} just uploaded a new track: '${song.title}'!",
                type = "upload"
            ))
        }
        return songId
    }

    suspend fun incrementPlayCount(songId: Int) {
        val current = dao.getSongByIdDirect(songId) ?: return
        dao.updateSong(current.copy(plays = current.plays + 1))
    }

    fun searchSongs(query: String): Flow<List<Song>> = dao.searchSongs(query)


    // --- COMMENTS ---
    fun getCommentsBySong(songId: Int): Flow<List<Comment>> = dao.getCommentsBySong(songId)

    suspend fun insertComment(songId: Int, userId: String, userName: String, text: String) {
        val comment = Comment(
            songId = songId,
            userId = userId,
            userName = userName,
            text = text
        )
        dao.insertComment(comment)

        // Notify artist of comment
        val song = dao.getSongByIdDirect(songId)
        if (song != null && song.artistId != userId) {
            dao.insertNotification(Notification(
                userId = song.artistId,
                title = "New Comment",
                content = "$userName commented: \"$text\" on ${song.title}",
                type = "comment"
            ))
        }
    }


    // --- MESSAGES / CHAT ---
    fun getMessagesBetween(userIdA: String, userIdB: String): Flow<List<Message>> =
        dao.getMessagesBetween(userIdA, userIdB)

    fun getMessagesForUser(userId: String): Flow<List<Message>> =
        dao.getMessagesForUser(userId)

    suspend fun sendMessage(senderId: String, senderName: String, receiverId: String, text: String) {
        val message = Message(
            senderId = senderId,
            senderName = senderName,
            receiverId = receiverId,
            text = text
        )
        dao.insertMessage(message)

        // Push notification of chat message to receiver
        dao.insertNotification(Notification(
            userId = receiverId,
            title = "New Direct Message",
            content = "You have a new message from $senderName: \"$text\"",
            type = "message"
        ))
    }


    // --- SOCIAL RELATIONSHIPS ---
    fun getSocialAction(userId: String, songId: Int): Flow<SocialAction?> =
        dao.getSocialAction(userId, songId)

    suspend fun toggleLike(userId: String, userName: String, songId: Int) {
        val currentSong = dao.getSongByIdDirect(songId) ?: return
        val currentAction = dao.getSocialActionDirect(userId, songId)
        
        val newLikedState = if (currentAction != null) !currentAction.isLiked else true
        val updatedAction = (currentAction ?: SocialAction(id = "${userId}_$songId", userId = userId, songId = songId))
            .copy(isLiked = newLikedState)
        
        dao.insertSocialAction(updatedAction)

        val likesDelta = if (newLikedState) 1 else -1
        dao.updateSong(currentSong.copy(likes = maxOf(0, currentSong.likes + likesDelta)))

        // Trigger notification on like
        if (newLikedState && currentSong.artistId != userId) {
            dao.insertNotification(Notification(
                userId = currentSong.artistId,
                title = "Song Liked ❤️",
                content = "$userName liked your song '${currentSong.title}'",
                type = "like"
            ))
        }
    }

    suspend fun toggleRepost(userId: String, userName: String, songId: Int) {
        val currentSong = dao.getSongByIdDirect(songId) ?: return
        val currentAction = dao.getSocialActionDirect(userId, songId)
        
        val newRepostState = if (currentAction != null) !currentAction.isReposted else true
        val updatedAction = (currentAction ?: SocialAction(id = "${userId}_$songId", userId = userId, songId = songId))
            .copy(isReposted = newRepostState)
        
        dao.insertSocialAction(updatedAction)

        val repostDelta = if (newRepostState) 1 else -1
        dao.updateSong(currentSong.copy(reposts = maxOf(0, currentSong.reposts + repostDelta)))

        if (newRepostState && currentSong.artistId != userId) {
            dao.insertNotification(Notification(
                userId = currentSong.artistId,
                title = "Song Reposted 🔁",
                content = "$userName reposted your song '${currentSong.title}' to their feed",
                type = "repost"
            ))
        }
    }

    fun getLikedSongs(userId: String): Flow<List<Song>> = dao.getLikedSongs(userId)


    // --- FOLLOWS ---
    fun getFollowState(followerId: String, followedId: String): Flow<Follow?> =
        dao.getFollowState(followerId, followedId)

    suspend fun toggleFollow(followerId: String, followerName: String, followedId: String) {
        val existing = dao.getFollowStateDirect(followerId, followedId)
        val targetArtist = dao.getArtistByIdDirect(followedId) ?: return
        val followerArtist = dao.getArtistByIdDirect(followerId) ?: return

        if (existing != null) {
            // Unfollow
            dao.deleteFollow(existing)
            dao.updateArtist(targetArtist.copy(followers = maxOf(0, targetArtist.followers - 1)))
            dao.updateArtist(followerArtist.copy(following = maxOf(0, followerArtist.following - 1)))
        } else {
            // Follow
            dao.insertFollow(Follow(id = "${followerId}_$followedId", followerId = followerId, followedId = followedId))
            dao.updateArtist(targetArtist.copy(followers = targetArtist.followers + 1))
            dao.updateArtist(followerArtist.copy(following = followerArtist.following + 1))

            // Notify followee
            dao.insertNotification(Notification(
                userId = followedId,
                title = "New Follower",
                content = "$followerName is now following your artist journey!",
                type = "follow"
            ))
        }
    }


    // --- BEATS MARKETPLACE ---
    val allBeats: Flow<List<BeatListing>> = dao.getAllBeats()

    suspend fun insertBeat(beat: BeatListing) = dao.insertBeat(beat)


    // --- NOTIFICATIONS ---
    fun getNotificationsForUser(userId: String): Flow<List<Notification>> =
        dao.getNotificationsForUser(userId)

    // --- STATUSES ---
    val allStatuses: Flow<List<Status>> = dao.getAllStatuses()

    suspend fun insertStatus(status: Status) = dao.insertStatus(status)

    // --- REELS ---
    val allReels: Flow<List<Reel>> = dao.getAllReels()

    fun getReelsByArtist(artistId: String): Flow<List<Reel>> = dao.getReelsByArtist(artistId)

    suspend fun insertReel(reel: Reel): Int {
        return dao.insertReel(reel).toInt()
    }

    suspend fun incrementReelLikes(reelId: Int) {
        dao.incrementReelLikes(reelId)
    }
}
