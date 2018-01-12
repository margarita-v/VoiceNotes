package com.margarita.voicenotes.common

import android.content.Context
import android.net.Uri
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Function for simple inflating layout file to view group
 * @param layoutRes ID of layout file which will be inflated to the view group
 * @param attachToRoot Flag which shows if we need to attach to the root this view
 * @return View with inflated layout
 */
fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

/**
 * Function for loading image to ImageView using image Uri
 * @param context Context of function call
 * @param uri Uri of image
 */
fun ImageView.loadImage(context: Context, uri: Uri) {
    Picasso.with(context).load(uri).into(this)
}

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
    val date = Date(this * TIME_UNIT)

    val calendarDate = Calendar.getInstance()
    val now = Calendar.getInstance()
    calendarDate.time = date

    val sdf = SimpleDateFormat(DEFAULT_PATTERN, Locale.getDefault())

    if (calendarDate.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
        val isToday = calendarDate.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)
        sdf.applyLocalizedPattern(if (isToday) TODAY_PATTERN else SAME_YEAR_PATTERN)
    }
    return sdf.format(date)
}

//region Constants for creation an image file
private const val DATE_FORMAT = "yyyyMMdd_HHmmss"
private const val IMAGE_PREFIX = "JPEG_"
private const val IMAGE_EXTENSION = ".jpg"
//endregion

/**
 * Function for creation a file of the note's image
 * @param storageDir Inner file storage dir of current application
 * @return File of the note's image
 */
fun createImageFile(storageDir: File): File {
    val timeStamp = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
    val imageFileName = IMAGE_PREFIX + timeStamp
    return File.createTempFile(imageFileName, IMAGE_EXTENSION, storageDir)
}