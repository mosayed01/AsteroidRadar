package com.udacity.asteroidradar.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.domain.models.Asteroid
import com.udacity.asteroidradar.domain.models.PictureOfDay
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [Asteroid::class, PictureOfDay::class], version = 1, exportSchema = false)
abstract class AsteroidDB : RoomDatabase() {
    abstract fun AsteroidDao(): AsteroidDao

    companion object {
        @Volatile
        lateinit var INSTANCE: AsteroidDB


        @Suppress("OPT_IN_IS_NOT_ENABLED")
        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context): AsteroidDB {
            synchronized(AsteroidDB::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDB::class.java,
                        "asteroid_table"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}