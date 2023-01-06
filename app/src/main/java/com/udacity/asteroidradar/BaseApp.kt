package com.udacity.asteroidradar

import android.app.Application
import com.udacity.asteroidradar.database.AsteroidDB
import kotlinx.coroutines.InternalCoroutinesApi
import timber.log.Timber

class BaseApp : Application() {


    @InternalCoroutinesApi
    override fun onCreate() {
        super.onCreate()
        AsteroidDB.getDatabase(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree());
        }
    }
}