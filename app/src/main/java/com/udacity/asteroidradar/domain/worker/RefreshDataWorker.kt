package com.udacity.asteroidradar.domain.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.data.local.AsteroidDB.Companion.getDatabase
import com.udacity.asteroidradar.domain.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshDataWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    companion object {
        const val WORK_NAME = "RefreshData"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidRepository(database)
        return try {
            repository.clearAsteroid()
            repository.refreshAsteroid()
            repository.refreshPicture()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}
