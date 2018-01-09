package com.margarita.voicenotes.common

import android.content.Context
import android.support.annotation.StringRes
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

/**
 * Function for showing a simple Toast message
 * @param messageRes String resource ID for a message text
 * @param duration Duration of the Toast message
 */
fun Context.showToast(@StringRes messageRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, messageRes, duration).show()
}

//region Constants for date parsing
private const val TIME_UNIT = 1000
private const val DEFAULT_PATTERN = "dd.MM.yy в H:mm"
private const val SAME_YEAR_PATTERN = "d MMM в H:mm"
private const val TODAY_PATTERN = "сегодня в H:mm"
//endregion

/**
 * Function for parsing date to String
 * @return Date in a String format
 */
fun Long.parseDate(): String {
    val locale = Locale.getDefault()
    val date = Date(this * TIME_UNIT)

    val calendarDate = Calendar.getInstance()
    val now = Calendar.getInstance()
    calendarDate.time = date

    val sdf = SimpleDateFormat(DEFAULT_PATTERN, locale)

    if (calendarDate.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
        val isToday = calendarDate.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)
        sdf.applyLocalizedPattern(if (isToday) TODAY_PATTERN else SAME_YEAR_PATTERN)
    }
    return sdf.format(date)
}
