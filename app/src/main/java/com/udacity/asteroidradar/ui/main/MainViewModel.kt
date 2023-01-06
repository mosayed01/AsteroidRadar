package com.udacity.asteroidradar.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.domain.models.Asteroid
import com.udacity.asteroidradar.domain.models.PictureOfDay
import com.udacity.asteroidradar.domain.repository.AsteroidRepository
import com.udacity.asteroidradar.util.FilterEvent
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel : ViewModel() {

    private val _listOfAsteroid = MutableLiveData<ArrayList<Asteroid>>(ArrayList())
    val listOfAsteroid: LiveData<ArrayList<Asteroid>>
        get() = _listOfAsteroid

    private val _picDay = MutableLiveData<PictureOfDay>()
    val picDay: LiveData<PictureOfDay>
        get() = _picDay

    private val repository = AsteroidRepository()

    init {
        setList()
        setPic()
    }

    fun setList(filterEvent: FilterEvent = FilterEvent.SavedEvent) {
        viewModelScope.launch {
            repository.setAsteroidsFromApiToLocal()

            when (filterEvent) {
                is FilterEvent.SavedEvent -> {
                    _listOfAsteroid.postValue(repository.getDataFromDB())
                }

                is FilterEvent.TodayEvent -> {
                    _listOfAsteroid.postValue(repository.getDataFromDBToday())
                }

                is FilterEvent.WeekEvent -> {
                    _listOfAsteroid.postValue(repository.getDataFromDBWeek())
                }
            }

        }
    }

    private fun setPic() {
        viewModelScope.launch {
            try {
                _picDay.postValue(repository.getPicture())
            } catch (e: Exception) {
                Timber.e("setPic: ${e.message ?: e.toString()}")
            }

        }
    }
}
