package com.margarita.voicenotes.common

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.margarita.voicenotes.R
import com.margarita.voicenotes.ui.fragments.BaseFragment
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Function for simple inflating layout file to view group
 * @param layoutRes ID of layout file which will be inflated to the view group
 * @param attachToRoot Flag which shows if we need to attach to the root this view
 * @return View with inflated layout
 */
fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View
        = LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

/**
 * Function for loading image to ImageView using image Uri
 * @param context Context of function call
 * @param uri Uri of image
 */
fun ImageView.loadImage(context: Context, uri: Uri?) {
    if (uri != null) {
        Glide.with(context)
                .load(uri)
                .into(this)
    }
}

/**
 * Function for setting filter for an image button's background and its drawable resource
 * @param enabled Flag which shows if we should set or clear filter
 */
fun ImageView.setEnabledIconColor(enabled: Boolean) {
    isEnabled = enabled
    if (enabled) {
        background?.colorFilter = null
        drawable?.colorFilter = null
    } else {
        background?.setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN)
        drawable?.setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN)
    }
}

/**
 * Function for setting a default image to image view
 * @param context Context of function call
 */
fun ImageView.setDefaultImage(context: Context): Unit
        = setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.ic_launcher))

/**
 * Function for showing a simple Toast message
 * @param messageRes String resource ID for a message text
 * @param duration Duration of the Toast message
 */
fun Context.showToast(@StringRes messageRes: Int, duration: Int = Toast.LENGTH_SHORT): Unit
        = Toast.makeText(this, messageRes, duration).show()

// Requested size of image cropping area
private const val IMAGE_REQUESTED_SIZE = 400
// Min size of the result of image cropping
private const val MIN_IMAGE_SIZE = 200

/**
 * Function for showing a crop activity
 * @param imageUri Uri of image which will be cropped
 */
fun Activity.showCropActivity(imageUri: Uri): Unit
        = CropImage.activity(imageUri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setActivityTitle(getString(R.string.crop_image))
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .setRequestedSize(IMAGE_REQUESTED_SIZE, IMAGE_REQUESTED_SIZE)
            .setMinCropResultSize(MIN_IMAGE_SIZE, MIN_IMAGE_SIZE)
            .setMultiTouchEnabled(true)
            .setFixAspectRatio(true)
            .setCropMenuCropButtonTitle(getString(R.string.done))
            .start(this)

//region Constants for date parsing
private const val DEFAULT_PATTERN = "dd.MM.yy в H:mm"
private const val SAME_YEAR_PATTERN = "d MMM в H:mm"
private const val TODAY_PATTERN = "сегодня в H:mm"
//endregion

/**
 * Function for parsing date to String
 * @return Date in a String format
 */
fun Long.parseDate(): String {
    val date = Date(this)

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

/**
 * Function for showing the view
 */
fun View.show() {
    visibility = View.VISIBLE
}

/**
 * Function for hiding the view
 */
fun View.hide() {
    visibility = View.GONE
}

/**
 * Function for setup the view's visibility
 * @param isVisible Flag which shows if the view should be visible
 */
fun View.setVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

/**
 * Function which implements a fragment replacement
 * @param containerId ID of container view for a fragment
 * @param fragment Fragment which will be shown
 * @param tag Fragment's tag
 */
fun FragmentManager.replace(@IdRes containerId: Int,
                            fragment: BaseFragment,
                            tag: String) {
    beginTransaction()
            .replace(containerId, fragment, tag)
            .commit()
}

/**
 * Function for setting the default title to the contextual toolbar
 */
fun ActionMode.setDefaultTitle(): Unit = setTitle(R.string.selected_note)

/**
 * Function for setting the title with a selected items count to the contextual toolbar
 * @param context Context of function call
 * @param count Count of selected items
 */
fun ActionMode.setSelectedItemsCount(context: Context, count: Int) {
    if (count > 1) {
        title = context.getString(R.string.selected_notes_plural, count)
    } else {
        setDefaultTitle()
    }
}

/**
 * Function for parsing a string to Uri
 */
fun String.parseStringToUri(): Uri = Uri.parse(this)

/**
 * Function for parsing a Uri to string
 */
fun Uri.parseToString(): String = this.toString()