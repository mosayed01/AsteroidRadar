package com.udacity.asteroidradar.repository

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.TAG
import com.udacity.asteroidradar.api.AsteroidAPI
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException

class AsteroidRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            )
        )
        .build()

    private val retrofitApi: AsteroidAPI by lazy {
        retrofit.create(AsteroidAPI::class.java)
    }

    private val db = AsteroidDB.INSTANCE!!.AsteroidDao()
    private val startAndEnd = getNextSevenDaysFormattedDates()

    suspend fun getDataFromDB(): ArrayList<Asteroid> {
        return withContext(Dispatchers.IO) {
            ArrayList(db.getAll())
        }
    }

    suspend fun getDataFromDBToday(): ArrayList<Asteroid> {
        return withContext(Dispatchers.IO) {
            ArrayList(db.getAsteroidsBySpecificDateOrder(startAndEnd[0], startAndEnd[0]))
        }
    }

    suspend fun getDataFromDBWeek(): ArrayList<Asteroid> {
        return withContext(Dispatchers.IO) {
            ArrayList(db.getAsteroidsBySpecificDateOrder(startAndEnd[0], startAndEnd[7]))
        }
    }

    suspend fun getPicture(): PictureOfDay {
        return withContext(Dispatchers.IO) {
            retrofitApi.getPicture()
        }
    }

    suspend fun setAsteroidsFromApiToLocal() {
        try {
            withContext(Dispatchers.IO) {
                val remoteAsteroids = retrofitApi.getAsteroidList(startAndEnd[0], startAndEnd[7])
                val dataAsParsed = parseAsteroidsJsonResult(JSONObject(remoteAsteroids))
                db.insertAll(dataAsParsed)
            }
        } catch (io: IOException) {
            Log.e(TAG, "setAsteroidsFromApiToLocal(io): ${io.localizedMessage ?: io.toString()}")
        } catch (http: HttpException) {
            Log.e(TAG, "setAsteroidsFromApiToLocal(http): ${http.message ?: http.toString()}")
        } catch (e: Exception) {
            Log.e(TAG, "setAsteroidsFromApiToLocal(e): ${e.message ?: e.toString()}")
        }
    }


}

