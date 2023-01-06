package com.udacity.asteroidradar.util

import com.udacity.asteroidradar.BuildConfig

object Constants {
    const val API_QUERY_DATE_FORMAT = "yyyy-mm-dd"
    const val DEFAULT_END_DATE_DAYS = 7
    const val BASE_URL = "https://api.nasa.gov/"
    const val API_KEY = BuildConfig.NASA_API_KEY
    const val NEXT_WEEK = 0
    const val LAST_WEEK = 1
}