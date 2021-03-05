package com.example.androiddevchallenge.utils

import java.text.SimpleDateFormat
import java.util.Locale

object TimeUtils {

    fun formatMillisecondsToMinutesAndSeconds(durationInMilliseconds: Long): String {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        return dateFormat.format(durationInMilliseconds)
    }

}