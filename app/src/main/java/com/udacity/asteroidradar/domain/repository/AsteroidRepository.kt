package com.udacity.asteroidradar.domain.repository

import androidx.lifecycle.LiveData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.data.local.AsteroidDB
import com.udacity.asteroidradar.data.remote.Api
import com.udacity.asteroidradar.data.remote.AsteroidAPI
import com.udacity.asteroidradar.data.remote.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.data.remote.getPrevSevenDaysFormattedDates
import com.udacity.asteroidradar.data.remote.parseAsteroidsJsonResult
import com.udacity.asteroidradar.domain.models.Asteroid
import com.udacity.asteroidradar.domain.models.PictureOfDay
import com.udacity.asteroidradar.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import timber.log.Timber
import java.io.IOException

class AsteroidRepository(database: AsteroidDB) {
    private val asteroidDao = database.AsteroidDao()

    val pictureOfDay: LiveData<PictureOfDay> = asteroidDao.getPictureOfDay()

    suspend fun refreshPicture() {
        withContext(Dispatchers.IO) {
            val pOfTheDay = Api.retrofitService.getPicture()
            asteroidDao.insert(pOfTheDay)
        }
    }

    suspend fun clearAsteroid() {
        withContext(Dispatchers.IO) {
            asteroidDao.clear()
        }
    }

    suspend fun refreshAsteroid() {
        lastWeek()
        nextWeek()
    }

    private suspend fun lastWeek() {
        withContext(Dispatchers.IO) {
            val prevWeek = getPrevSevenDaysFormattedDates()
            val asteroids =
                parseAsteroidsJsonResult(
                    JSONObject(
                        Api.retrofitService
                            .getAsteroidList(startDate = prevWeek.first(), endDate = prevWeek.last())
                    ), Constants.LAST_WEEK
                )
            asteroidDao.insertAll(ArrayList(asteroids))
        }
    }

    private suspend fun nextWeek() {
        withContext(Dispatchers.IO) {
            val nextWeek = getNextSevenDaysFormattedDates()
            val asteroids =
                parseAsteroidsJsonResult(
                    JSONObject(
                        Api.retrofitService
                            .getAsteroidList(startDate = nextWeek.first(), endDate = nextWeek.last())
                    ), Constants.NEXT_WEEK
                )
            asteroidDao.insertAll(ArrayList(asteroids))
        }
    }

}

