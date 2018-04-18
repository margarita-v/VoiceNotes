package com.margarita.voicenotes.ui.activities.creation.note

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import com.margarita.voicenotes.R
import com.margarita.voicenotes.ui.activities.creation.BaseNewItemActivity
import com.margarita.voicenotes.ui.fragments.creation.NewNoteFragment
import com.margarita.voicenotes.ui.fragments.dialogs.confirm.ConfirmDialogFragment
import java.io.File

/**
 * Activity for creation of notes
 */
open class NewNoteActivity :
        BaseNewItemActivity(R.string.create_note),
        NewNoteFragment.SelectedOption,
        ConfirmDialogFragment.ConfirmationListener {

    /**
     * Storage of static constants
     */
    private companion object {

        /**
         * Type for intent for image picking
         */
        const val IMAGE_INTENT_TYPE = "image/*"

        /**
         * Value for default photo request code when nothing is chosen
         */
        const val DEFAULT_PHOTO_REQUEST_CODE = -1

        /**
         * Key for Bundle for saving previous and last photo request codes
         */
        const val PREVIOUS_PHOTO_REQUEST_CODE = "PREVIOUS_PHOTO_REQUEST_CODE"
        const val LAST_PHOTO_REQUEST_CODE = "LAST_PHOTO_REQUEST_CODE"
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
    protected var newPhotoFile: File? = null

    /**
     * Uri for a new photo
     */
    private var newPhotoUri: Uri? = null

    /**
     * Fragment for creation of notes
     */
    protected lateinit var newNoteFragment: NewNoteFragment

    /**
     * Codes of previous and last requests for a photo addition
     */
    private var previousPhotoRequestCode = DEFAULT_PHOTO_REQUEST_CODE
    private var lastPhotoRequestCode = DEFAULT_PHOTO_REQUEST_CODE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Try to restore fragment
        newNoteFragment = restoreFragment() as NewNoteFragment? ?: NewNoteFragment()
        fragment = newNoteFragment
        setFragment(fragment)

        if (savedInstanceState != null) {
            newPhotoFile = savedInstanceState.getSerializable(PHOTO_FILE_KEY) as File?
            newPhotoUri = savedInstanceState.getParcelable(PHOTO_URI_KEY) as Uri?
            previousPhotoRequestCode =
                    getPhotoRequestCode(savedInstanceState, PREVIOUS_PHOTO_REQUEST_CODE)
            lastPhotoRequestCode =
                    getPhotoRequestCode(savedInstanceState, LAST_PHOTO_REQUEST_CODE)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (!isNoteCreated && outState != null) {
            outState.putSerializable(PHOTO_FILE_KEY, newPhotoFile)
            outState.putParcelable(PHOTO_URI_KEY, newPhotoUri)
            outState.putInt(PREVIOUS_PHOTO_REQUEST_CODE, previousPhotoRequestCode)
            outState.putInt(LAST_PHOTO_REQUEST_CODE, lastPhotoRequestCode)
        }
    }

    override fun onBackPressed() {
        ConfirmDialogFragment.newInstance(getExitMessageResId())
                .show(supportFragmentManager, ConfirmDialogFragment.SHOWING_TAG)
    }

    override fun confirm(tag: String) {
        when (tag) {
            ConfirmDialogFragment.DELETE_CONFIRM_TAG -> deletePhoto()
            else -> {
                // Cancel note creation
                if (!isNoteCreated) {
                    deletePhotoFile(newPhotoFile)
                    deletePhotoFile(newNoteFragment.photoFile)
                }
                finish()
            }
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
            newPhotoFile = createPhotoFile()
            newPhotoUri = getPhotoUri(newPhotoFile!!)
            showPhotoActivity(takePhotoIntent, newPhotoUri!!)
        }
    }

    override fun cropImage(photoUri: Uri?): Unit = crop(photoUri)

    /**
     * Function for removing a photo of note
     */
    private fun deletePhoto() {
        // If a new photo file is null, we should delete an old saved file
        if (newPhotoFile == null) {
            deletePhotoFile(newNoteFragment.photoFile)
            newNoteFragment.photoFile = null
        } else {
            // Delete a new photo file
            deletePhotoFile(newPhotoFile)
            newPhotoFile = null
        }

        // Call fragment's method to change UI.
        // If a new photo uri was not set (if we want to delete an existing photo)
        // or if we delete a new photo
        if (newPhotoUri == null || newNoteFragment.photoUri == newPhotoUri) {
            newNoteFragment.deletePhoto()
        }
        newPhotoUri = null
    }

    /**
     * Function for getting a string resource ID for a correct exit message
     */
    protected open fun getExitMessageResId(): Int = R.string.confirm_cancel_note_create

    /**
     * Function for getting a photo request code from Bundle
     */
    private fun getPhotoRequestCode(bundle: Bundle, key: String): Int
            = bundle.getInt(key, DEFAULT_PHOTO_REQUEST_CODE)

    /**
     * Function for removing an old photo file of note and setting a new photo file
     */
    private fun deleteAndSetPhoto(photoFile: File? = null) {
        deletePhotoFile(newNoteFragment.photoFile)
        newNoteFragment.photoFile = photoFile
    }

    /**
     * Function for changing saved photo request codes and performing an image cropping
     */
    private fun cropAndSetRequestCode(requestCode: Int) {
        crop(newPhotoUri)
        previousPhotoRequestCode = lastPhotoRequestCode
        lastPhotoRequestCode = requestCode
    }

    /**
     * Function for receiving an activity's result
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != SPEECH_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_CANCELED -> {
                    if (lastPhotoRequestCode != PICK_PHOTO_REQUEST_CODE) {
                        deletePhoto()
                    }
                    // Cancel of saving photo request codes
                    lastPhotoRequestCode = previousPhotoRequestCode
                    previousPhotoRequestCode = DEFAULT_PHOTO_REQUEST_CODE
                }
                Activity.RESULT_OK -> {
                    when (requestCode) {
                        // Result of a choosing photo from gallery
                        PICK_PHOTO_REQUEST_CODE -> {
                            newPhotoUri = data?.data
                            cropAndSetRequestCode(PICK_PHOTO_REQUEST_CODE)
                        }

                        // Result of taking photo
                        TAKE_PHOTO_REQUEST_CODE ->
                            // A new photo uri was set before starting the photo activity
                            cropAndSetRequestCode(TAKE_PHOTO_REQUEST_CODE)

                        // Result of image cropping
                        CROP_PHOTO_REQUEST_CODE -> {
                            if (newPhotoFile != null) {
                                // Set a new photo file
                                deleteAndSetPhoto(newPhotoFile)
                                newPhotoFile = null
                            } else if (lastPhotoRequestCode == PICK_PHOTO_REQUEST_CODE) {
                                // Photo was picked from gallery,
                                // so we should to delete an old photo file
                                deleteAndSetPhoto()
                            }

                            // Set a new photo Uri
                            if (newPhotoUri != null) {
                                newNoteFragment.photoUri = newPhotoUri
                            }
                            newPhotoUri = null
                            newNoteFragment.setCroppedPhoto(getCroppedPhoto(data))
                        }
                    }
                } // RESULT_OK
            } // when
        } // if
    } // fun
}
