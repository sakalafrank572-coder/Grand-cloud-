package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artist_profiles")
data class ArtistProfile(
    @PrimaryKey val id: String, // Acts as username & unique ID
    val displayName: String,
    val email: String,
    val bio: String,
    val avatarGradientStart: String, // HEX value
    val avatarGradientEnd: String, // HEX value
    val isArtist: Boolean = true,
    val isVerified: Boolean = false,
    val followers: Int = 0,
    val following: Int = 0,
    val password: String = "password",
    val avatarUri: String? = null
)

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val artistId: String,
    val artistName: String,
    val genre: String,
    val description: String,
    val durationSec: Int = 180,
    val plays: Int = 0,
    val likes: Int = 0,
    val reposts: Int = 0,
    val coverColorStart: String, // HEX gradient for cover art
    val coverColorEnd: String,   // HEX gradient for cover art
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val songId: Int,
    val userId: String,
    val userName: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val senderId: String,
    val senderName: String,
    val receiverId: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "social_actions")
data class SocialAction(
    @PrimaryKey val id: String, // Format: "${userId}_${songId}"
    val userId: String,
    val songId: Int,
    val isLiked: Boolean = false,
    val isReposted: Boolean = false
)

@Entity(tableName = "follows")
data class Follow(
    @PrimaryKey val id: String, // Format: "${followerId}_${followedId}"
    val followerId: String,
    val followedId: String
)

@Entity(tableName = "beat_listings")
data class BeatListing(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val producerId: String,
    val producerName: String,
    val price: String = "$29.99",
    val bpm: Int = 140,
    val tags: String = "Trap,Dark"
)

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String, // Target user receiving notification
    val title: String,
    val content: String,
    val type: String, // "like", "follow", "comment", "message", "verify"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "statuses")
data class Status(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val artistId: String,
    val artistName: String,
    val text: String,
    val bgGradientStart: String = "#0288D1",
    val bgGradientEnd: String = "#26C6DA",
    val timestamp: Long = System.currentTimeMillis(),
    val videoUri: String? = null,
    val imageUri: String? = null,
    val linkUrl: String? = null
)

@Entity(tableName = "reels")
data class Reel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val artistId: String,
    val artistName: String,
    val title: String, // Caption
    val soundName: String = "Original Audio",
    val durationSec: Int = 15,
    val likes: Int = 0,
    val shares: Int = 0,
    val coverColorStart: String = "#FF9F00",
    val coverColorEnd: String = "#FFDF7A",
    val timestamp: Long = System.currentTimeMillis()
)

