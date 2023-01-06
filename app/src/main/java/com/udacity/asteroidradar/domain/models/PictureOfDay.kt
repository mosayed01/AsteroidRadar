package com.udacity.asteroidradar.domain.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "Picture_of_day_table")
@Parcelize
data class PictureOfDay(
    val date: String,
    @Json(name = "media_type")
    val mediaType: String,
    val title: String,
    @PrimaryKey(autoGenerate = false)
    val url: String
) : Parcelable