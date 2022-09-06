package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.TAG
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.util.FilterEvent
import kotlinx.coroutines.launch

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
                Log.e(TAG, "setPic: ${e.message ?: e.toString()}")
            }

        }
    }

//    private fun setList() {
//        viewModelScope.launch {
//
//            val startAndEnd = getNextSevenDaysFormattedDates()
//            try {
//                val result = repository.getAsteroidListFromApi(startAndEnd[0], startAndEnd[7])
//                val listData = parseAsteroidsJsonResult(JSONObject(result))
//
//                repository.addToDB(listData)
//
//
//                _listOfAsteroid.postValue(repository.getDataFromDB())
//            } catch (exception: Exception) {
//
//            }
//        }
//    }

//    fun getAsteroid(filterEvent: FilterEvent = FilterEvent.SavedEvent) {
//        viewModelScope.launch {
//            var bySaved: List<Asteroid> = emptyList()
//            var byToday: List<Asteroid>
//            var byWeek: List<Asteroid>
//
//            val asteroidAsync = CoroutineScope(Dispatchers.IO).async(start = CoroutineStart.LAZY) {
//                repository.setAsteroidsFromApiToLocal()
//                bySaved = mDao.getAll()
//            }
//
//            asteroidAsync.await()
//
//            try {
//                when (filterEvent) {
//                    is FilterEvent.SavedEvent -> {
//                        _listOfAsteroid.postValue(repository.getDataFromDB())
//                    }
//
//                    is FilterEvent.TodayEvent -> {
//                        withContext(Dispatchers.IO) {
//                            byToday =
//                                mDao.getAsteroidsBySpecificDateEvent(startAndEnd[0], startAndEnd[0])
//                        }
//                        _listOfAsteroid.postValue(ArrayList(byToday))
//                    }
//
//                    is FilterEvent.WeekEvent -> {
//                        withContext(Dispatchers.IO) {
//                            byWeek =
//                                mDao.getAsteroidsBySpecificDateEvent(startAndEnd[0], startAndEnd[7])
//                        }
//                        _listOfAsteroid.postValue(ArrayList(byWeek))
//                    }
//                }
//            } catch (e: IOException) {
//                Log.e(TAG, "invoke: ${e.localizedMessage ?: e.toString()}")
////                _listOfAsteroid.postValue(repository.getDataFromDB())
//            } catch (e: HttpException) {
//                Log.e(TAG, "invoke: ${e.localizedMessage ?: e.toString()}")
////                _listOfAsteroid.postValue(repository.getDataFromDB())
//            } catch (e: Exception) {
//                Log.e(TAG, "invoke: ${e.localizedMessage ?: e.toString()}")
////                _listOfAsteroid.postValue(repository.getDataFromDB())
//            }
//
//        }
//    }
}
