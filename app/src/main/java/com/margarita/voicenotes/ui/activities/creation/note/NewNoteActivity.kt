package com.margarita.voicenotes.ui.activities.creation.note

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import com.margarita.voicenotes.R
import com.margarita.voicenotes.ui.activities.creation.BaseNewItemActivity
import com.margarita.voicenotes.ui.fragments.creation.NewNoteFragment
import java.io.File

/**
 * Activity for creation of notes
 */
open class NewNoteActivity :
        BaseNewItemActivity(R.string.create_note),
        NewNoteFragment.SelectedOption {

    /**
     * Storage of static constants
     */
    private companion object {

        /**
         * Type for intent for image picking
         */
        const val IMAGE_INTENT_TYPE = "image/*"
    }

    /**
     * Intent for choosing photo from gallery
     */
    private val pickPhotoFromGalleryIntent by lazy {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                .setType(IMAGE_INTENT_TYPE)
    }

    /**
     * Current photo for a new note
     */
    protected var photoFile: File? = null

    /**
     * Uri for a new photo
     */
    private var newPhotoUri: Uri? = null

    /**
     * Fragment for creation of notes
     */
    protected lateinit var newNoteFragment: NewNoteFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Try to restore fragment
        newNoteFragment = restoreFragment() as NewNoteFragment? ?: NewNoteFragment()
        fragment = newNoteFragment
        setFragment(fragment)

        photoFile = savedInstanceState?.getSerializable(PHOTO_FILE_KEY) as File?
        newPhotoUri = savedInstanceState?.getParcelable(PHOTO_URI_KEY) as Uri?
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable(PHOTO_FILE_KEY, photoFile)
        outState?.putParcelable(PHOTO_URI_KEY, newPhotoUri)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isNoteCreated) {
            deletePhotoFile(photoFile)
        }
    }

    override fun usedForCreation(): Boolean = true

    override fun pickImageFromGallery() {
        if (checkIntentHandlers(pickPhotoFromGalleryIntent)) {
            startActivityForResult(pickPhotoFromGalleryIntent, PICK_PHOTO_REQUEST_CODE)
        }
    }

    override fun takePhoto() {
        val takePhotoIntent = createPhotoIntent()
        if (checkIntentHandlers(takePhotoIntent)) {
            photoFile = createPhotoFile()
            newPhotoUri = getPhotoUri(photoFile!!)
            showPhotoActivity(takePhotoIntent, newPhotoUri!!)
        }
    }

    override fun cropImage(photoUri: Uri?): Unit = crop(photoUri)

    override fun deletePhoto() {
        deletePhotoFile(photoFile)
        if (newPhotoUri == null || newNoteFragment.photoUri == newPhotoUri) {
            newNoteFragment.deletePhoto()
        }
    }

    /**
     * Function for receiving an activity's result
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != SPEECH_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_CANCELED -> {
                    if (requestCode != PICK_PHOTO_REQUEST_CODE) {
                        deletePhoto()
                    }
                }
                Activity.RESULT_OK -> {
                    when (requestCode) {
                        // Result of a choosing photo from gallery
                        PICK_PHOTO_REQUEST_CODE -> {
                            newNoteFragment.photoUri = data?.data
                            crop(newNoteFragment.photoUri)
                        }

                        // Result of taking photo
                        TAKE_PHOTO_REQUEST_CODE -> crop(newPhotoUri)

                        // Result of image cropping
                        CROP_PHOTO_REQUEST_CODE -> {
                            // If photo was picked from gallery,
                            // photoUri of fragment had been already set
                            if (newNoteFragment.photoUri == null) {
                                newNoteFragment.photoUri = newPhotoUri
                            }
                            newNoteFragment.setCroppedPhoto(getCroppedPhoto(data))
                        }
                    }
                } // RESULT_OK
            } // when
        } // if
    } // fun
}
