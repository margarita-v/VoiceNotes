package com.margarita.voicenotes.common

import com.margarita.voicenotes.models.entities.Category
import com.margarita.voicenotes.models.entities.NoteItem
import com.margarita.voicenotes.mvp.presenter.base.BaseDatabasePresenter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

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
 * Function for getting a note's category
 */
fun getCategory(noteItem: NoteItem): Category? = BaseDatabasePresenter.getCategory(noteItem.id)

/**
 * Function for getting a category name for the note
 */
fun getCategoryName(noteItem: NoteItem): String? = getCategory(noteItem)?.name