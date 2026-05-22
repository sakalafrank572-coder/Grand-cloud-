package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicPlatformDao {

    // --- ARTISTS & PROFILES ---
    @Query("SELECT * FROM artist_profiles WHERE id = :id")
    fun getArtistById(id: String): Flow<ArtistProfile?>

    @Query("SELECT * FROM artist_profiles WHERE id = :id")
    suspend fun getArtistByIdDirect(id: String): ArtistProfile?

    @Query("SELECT * FROM artist_profiles")
    fun getAllArtists(): Flow<List<ArtistProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtist(artist: ArtistProfile)

    @Update
    suspend fun updateArtist(artist: ArtistProfile)


    // --- SONGS ---
    @Query("SELECT * FROM songs ORDER BY timestamp DESC")
    fun getAllSongs(): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE id = :id")
    fun getSongById(id: Int): Flow<Song?>

    @Query("SELECT * FROM songs WHERE id = :id")
    suspend fun getSongByIdDirect(id: Int): Song?

    @Query("SELECT * FROM songs WHERE artistId = :artistId ORDER BY timestamp DESC")
    fun getSongsByArtist(artistId: String): Flow<List<Song>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: Song): Long

    @Update
    suspend fun updateSong(song: Song)

    @Query("""
        SELECT * FROM songs 
        WHERE title LIKE '%' || :query || '%' 
        OR artistName LIKE '%' || :query || '%' 
        OR genre LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%'
        ORDER BY timestamp DESC
    """)
    fun searchSongs(query: String): Flow<List<Song>>


    // --- COMMENTS ---
    @Query("SELECT * FROM comments WHERE songId = :songId ORDER BY timestamp ASC")
    fun getCommentsBySong(songId: Int): Flow<List<Comment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: Comment)


    // --- MESSAGES / CHAT ---
    @Query("""
        SELECT * FROM messages 
        WHERE (senderId = :userIdA AND receiverId = :userIdB) 
        OR (senderId = :userIdB AND receiverId = :userIdA) 
        ORDER BY timestamp ASC
    """)
    fun getMessagesBetween(userIdA: String, userIdB: String): Flow<List<Message>>

    @Query("SELECT * FROM messages WHERE senderId = :userId OR receiverId = :userId ORDER BY timestamp DESC")
    fun getMessagesForUser(userId: String): Flow<List<Message>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)


    // --- SOCIAL ACTIONS (Likes & Reposts) ---
    @Query("SELECT * FROM social_actions WHERE userId = :userId AND songId = :songId")
    fun getSocialAction(userId: String, songId: Int): Flow<SocialAction?>

    @Query("SELECT * FROM social_actions WHERE userId = :userId AND songId = :songId")
    suspend fun getSocialActionDirect(userId: String, songId: Int): SocialAction?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSocialAction(action: SocialAction)

    @Query("""
        SELECT * FROM songs 
        WHERE id IN (SELECT songId FROM social_actions WHERE userId = :userId AND isLiked = 1)
        ORDER BY timestamp DESC
    """)
    fun getLikedSongs(userId: String): Flow<List<Song>>


    // --- FOLLOWS ---
    @Query("SELECT * FROM follows WHERE followerId = :followerId AND followedId = :followedId")
    fun getFollowState(followerId: String, followedId: String): Flow<Follow?>

    @Query("SELECT * FROM follows WHERE followerId = :followerId AND followedId = :followedId")
    suspend fun getFollowStateDirect(followerId: String, followedId: String): Follow?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollow(follow: Follow)

    @Delete
    suspend fun deleteFollow(follow: Follow)


    // --- BEAT MARKETPLACE ---
    @Query("SELECT * FROM beat_listings ORDER BY id DESC")
    fun getAllBeats(): Flow<List<BeatListing>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBeat(beat: BeatListing)


    // --- NOTIFICATIONS ---
    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY timestamp DESC")
    fun getNotificationsForUser(userId: String): Flow<List<Notification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: Notification)

    // --- STATUSES ---
    @Query("SELECT * FROM statuses ORDER BY timestamp DESC")
    fun getAllStatuses(): Flow<List<Status>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatus(status: Status)

    // --- REELS ---
    @Query("SELECT * FROM reels ORDER BY timestamp DESC")
    fun getAllReels(): Flow<List<Reel>>

    @Query("SELECT * FROM reels WHERE artistId = :artistId ORDER BY timestamp DESC")
    fun getReelsByArtist(artistId: String): Flow<List<Reel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReel(reel: Reel): Long

    @Query("UPDATE reels SET likes = likes + 1 WHERE id = :reelId")
    suspend fun incrementReelLikes(reelId: Int)
}
