package com.margarita.voicenotes.ui.activities.creation.note

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.showCropActivity
import com.margarita.voicenotes.common.showToast
import com.margarita.voicenotes.ui.activities.creation.BaseNewItemActivity
import com.margarita.voicenotes.ui.fragments.creation.NewNoteFragment
import com.theartofdev.edmodo.cropper.CropImage
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
    private var photoFile: File? = null

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

        photoFile = savedInstanceState?.getSerializable(PHOTO_FILE_KEY) as File?
        newPhotoUri = savedInstanceState?.getParcelable(NEW_PHOTO_URI_KEY) as Uri?

        // Try to restore fragment
        newNoteFragment = restoreFragment() as NewNoteFragment? ?: NewNoteFragment()
        fragment = newNoteFragment
        setFragment(fragment)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable(PHOTO_FILE_KEY, photoFile)
        outState?.putParcelable(NEW_PHOTO_URI_KEY, newPhotoUri)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isNoteCreated) {
            deletePhotoFile()
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

    override fun cropImage(photoUri: Uri?) {
        if (photoUri != null) {
            showCropActivity(photoUri)
        } else {
            showToast(R.string.image_loading_error)
        }
    }

    override fun deletePhoto() {
        deletePhotoFile()
        if (newPhotoUri == null || newNoteFragment.photoUri == newPhotoUri) {
            newNoteFragment.deletePhoto()
        }
    }

    /**
     * Function for removing a photo file
     */
    private fun deletePhotoFile() {
        photoFile?.delete()
    }

    /**
     * Function for cropping image of note which is using Uri from the NewNoteFragment
     */
    private fun cropFragmentImage(photoUri: Uri?): Unit = cropImage(photoUri)

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
                            cropFragmentImage(newNoteFragment.photoUri)
                        }

                        // Result of taking photo
                        TAKE_PHOTO_REQUEST_CODE -> cropFragmentImage(newPhotoUri)

                        // Result of image cropping
                        CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                            // If photo was picked from gallery,
                            // photoUri of fragment had been already set
                            if (newNoteFragment.photoUri == null) {
                                newNoteFragment.photoUri = newPhotoUri
                            }
                            newNoteFragment.cropImage(CropImage.getActivityResult(data).uri)
                        }
                    }
                } // RESULT_OK
            } // when
        } // if
    } // fun
}
