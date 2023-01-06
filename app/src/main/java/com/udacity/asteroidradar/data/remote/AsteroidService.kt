package com.udacity.asteroidradar.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.domain.models.PictureOfDay
import com.udacity.asteroidradar.util.Constants
import com.udacity.asteroidradar.util.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrieve a list of Asteroids based on their closest approach date to Earth.
 * GET https://api.nasa.gov/neo/rest/v1/feed?start_date=START_DATE&end_date=END_DATE&api_key=API_KEY
 *
 * Lookup a specific Asteroid based on its NASA JPL small body (SPK-ID) ID
 * GET https://api.nasa.gov/neo/rest/v1/neo/
 *
 * Browse the overall Asteroid data-set
 * GET https://api.nasa.gov/neo/rest/v1/neo/browse/
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface AsteroidAPI {

    @GET(value = "neo/rest/v1/feed")
    suspend fun getAsteroidList(
        @Query(value = "start_date") startDate: String,
        @Query(value = "end_date") endDate: String,
        @Query(value = "api_key") apiKey: String = Constants.API_KEY
    ): String

    @GET("planetary/apod")
    suspend fun getPicture(
        @Query(value = "api_key")
        apiKey: String = Constants.API_KEY
    ): PictureOfDay
}

object Api {
    val retrofitService: AsteroidAPI by lazy { retrofit.create(AsteroidAPI::class.java) }
}

