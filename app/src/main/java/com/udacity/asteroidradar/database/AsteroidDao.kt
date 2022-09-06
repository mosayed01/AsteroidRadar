package com.udacity.asteroidradar.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {
    @Query("select * from asteroid_table ORDER BY closeApproachDate ASC")
    fun getAll(): List<Asteroid>


    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate >= :startDate AND closeApproachDate <= :endDate ORDER BY closeApproachDate ASC")
    fun getAsteroidsBySpecificDateOrder(startDate: String, endDate: String): List<Asteroid>


    @Query("DELETE FROM asteroid_table WHERE closeApproachDate < :today")
    fun deleteAllPreviousDayAsteroids(today: String): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroids: ArrayList<Asteroid>)
}