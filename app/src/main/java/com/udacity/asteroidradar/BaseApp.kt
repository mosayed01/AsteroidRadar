package com.udacity.asteroidradar

import android.app.Application
import com.udacity.asteroidradar.database.AsteroidDB
import kotlinx.coroutines.InternalCoroutinesApi

class BaseApp : Application() {


    @InternalCoroutinesApi
    override fun onCreate() {
        super.onCreate()
        AsteroidDB.getDatabase(this)
    }
}