package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ArtistProfile::class,
        Song::class,
        Comment::class,
        Message::class,
        SocialAction::class,
        Follow::class,
        BeatListing::class,
        Notification::class,
        Status::class,
        Reel::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun musicPlatformDao(): MusicPlatformDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aura_music_db"
                )
                .fallbackToDestructiveMigration()
                .addCallback(DatabaseCallback(context))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Seed database asynchronously on creation
            CoroutineScope(Dispatchers.IO).launch {
                val dao = getInstance(context).musicPlatformDao()
                seedDatabase(dao)
            }
        }

        private suspend fun seedDatabase(dao: MusicPlatformDao) {
            // Seeding disabled to ensure only real user accounts and submissions are collected.
        }
    }
}
