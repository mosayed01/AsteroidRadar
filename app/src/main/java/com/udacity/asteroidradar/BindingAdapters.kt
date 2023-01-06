package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.adapters.RecycleAdapter
import java.lang.reflect.Array.get

@BindingAdapter("statusIcon")
fun ImageView.bindAsteroidStatusImage(isHazardous: Boolean) {
    contentDescription = if (isHazardous) {
        setImageResource(R.drawable.ic_status_potentially_hazardous)
        "potentially hazardous"
    } else {
        setImageResource(R.drawable.ic_status_normal)
        "not hazardous"

    }
}

@BindingAdapter("asteroidStatusImage")
fun ImageView.bindDetailsStatusImage(isHazardous: Boolean) {
    contentDescription = if (isHazardous) {
        setImageResource(R.drawable.asteroid_hazardous)
        context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        setImageResource(R.drawable.asteroid_safe)
        context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("ms:bindList")
fun bindList(rv: RecyclerView, listData: ArrayList<Asteroid>) {
    val adapter = rv.adapter as RecycleAdapter
    adapter.submitList(listData)
}

@BindingAdapter("ms:pictureOfTheDay")
fun bindPicture(imageView: ImageView, url: String?) {
    Picasso.with(imageView.context)
        .load(url)
        .into(imageView)
    if (url == null) {
        imageView.setImageResource(R.drawable.placeholder_picture_of_day)
    }
}

@BindingAdapter("ms:bindContentDescription")
fun setPicOfTheDayContentDescription(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    if (pictureOfDay != null) {
        imageView.contentDescription = pictureOfDay.title
    }
}

@BindingAdapter("helpContentDescription")
fun setContent(imageView: ImageView, string: String?) {
    imageView.contentDescription = string
}