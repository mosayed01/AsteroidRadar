package com.udacity.asteroidradar.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.udacity.asteroidradar.domain.models.Asteroid
import com.udacity.asteroidradar.domain.models.PictureOfDay
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroids: ArrayList<Asteroid>)


    @Query("select * from asteroid_table ORDER BY closeApproachDate ASC")
    fun getAll(): Flow<List<Asteroid>>

    @Query("delete from asteroid_table")
    fun clear()

    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate >= :today ORDER BY closeApproachDate ASC")
    fun getAsteroidsNext(today: String): Flow<List<Asteroid>>

    @Query("SELECT * FROM  asteroid_table WHERE closeApproachDate == :today ORDER BY closeApproachDate DESC")
    fun getTodayAsteroid(today: String): Flow<List<Asteroid>>

    @Query("SELECT * FROM  asteroid_table WHERE closeApproachDate < :today ORDER BY closeApproachDate DESC")
    fun getSavedBeforeTodayAsteroid(today: String): Flow<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = PictureOfDay::class)
    suspend fun insert(pictureOfDayEntity: PictureOfDay)

    @Update
    suspend fun update(pictureOfDayEntity: PictureOfDay)

    @Query("SELECT * FROM  picture_of_day_table ORDER BY date DESC LIMIT 1")
    fun getPictureOfDay(): LiveData<PictureOfDay>
}