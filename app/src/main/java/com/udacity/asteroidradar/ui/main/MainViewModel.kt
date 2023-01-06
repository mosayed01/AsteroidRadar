package com.udacity.asteroidradar.ui.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.data.local.AsteroidDB.Companion.getDatabase
import com.udacity.asteroidradar.data.remote.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.data.remote.getToday
import com.udacity.asteroidradar.domain.models.Asteroid
import com.udacity.asteroidradar.domain.models.PictureOfDay
import com.udacity.asteroidradar.domain.repository.AsteroidRepository
import com.udacity.asteroidradar.util.FilterEvent
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(context: Context) : ViewModel() {
    private val database = getDatabase(context)
    private val repository = AsteroidRepository(database)
    private val dao = database.AsteroidDao()

    private val _listOfAsteroid = MutableLiveData<ArrayList<Asteroid>>(ArrayList())
    val listOfAsteroid: LiveData<ArrayList<Asteroid>>
        get() = _listOfAsteroid

    val picDay: LiveData<PictureOfDay> = repository.pictureOfDay

    init {
        showAll()
        setList()
        refreshPicture()
    }

    private fun showAll() {
        viewModelScope.launch {
            dao.getAll()
                .collect { _listOfAsteroid.value = ArrayList(it.take(7)) }
        }
    }

    fun setList(filterEvent: FilterEvent = FilterEvent.SavedEvent) {
        viewModelScope.launch {
            repository.refreshAsteroid()

            when (filterEvent) {
                is FilterEvent.SavedEvent -> {
                    dao.getSavedBeforeTodayAsteroid(getToday())
                        .collect { _listOfAsteroid.value = ArrayList(it) }
                }

                is FilterEvent.TodayEvent -> {
                    dao.getTodayAsteroid(getToday())
                        .collect { _listOfAsteroid.value = ArrayList(it) }
                }

                is FilterEvent.WeekEvent -> {
                    val week = getNextSevenDaysFormattedDates()
                    dao.getAsteroidsNext(week.first())
                        .collect { _listOfAsteroid.value = ArrayList(it) }
                }
            }

        }
    }

    private fun refreshPicture() {
        viewModelScope.launch {
            try {
                repository.refreshPicture()
            } catch (e: Exception) {
                Timber.e("setPic: ${e.message ?: e.toString()}")
            }
        }
    }
}
