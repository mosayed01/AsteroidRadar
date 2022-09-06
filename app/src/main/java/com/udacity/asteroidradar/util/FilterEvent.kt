package com.udacity.asteroidradar.util

sealed class FilterEvent {
    object WeekEvent: FilterEvent()
    object TodayEvent: FilterEvent()
    object SavedEvent: FilterEvent()

}
