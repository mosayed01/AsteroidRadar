package com.udacity.asteroidradar.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.domain.models.Asteroid
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [Asteroid::class], version = 1, exportSchema = false)
abstract class AsteroidDB : RoomDatabase() {
    abstract fun AsteroidDao(): AsteroidDao

    companion object {
        @Volatile
        var INSTANCE: AsteroidDB? = null


        @InternalCoroutinesApi
        fun getDatabase(context: Context): AsteroidDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AsteroidDB::class.java,
                    "asteroid_table"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}