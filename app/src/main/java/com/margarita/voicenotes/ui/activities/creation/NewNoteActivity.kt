package com.margarita.voicenotes.ui.activities.creation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.support.v4.content.FileProvider
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.createImageFile
import com.margarita.voicenotes.common.showCropActivity
import com.margarita.voicenotes.common.showToast
import com.margarita.voicenotes.ui.fragments.NewNoteFragment
import com.theartofdev.edmodo.cropper.CropImage

/**
 * Activity for a note creation, showing and editing
 */
class NewNoteActivity : BaseNewItemActivity(), NewNoteFragment.SelectedOption {

    /**
     * Storage of static constants
     */
    private companion object {

        /**
         * Authority for the application's file provider
         */
        const val FILE_PROVIDER_AUTHORITY = "com.margarita.voicenotes.android.fileprovider"

        /**
         * Request code for a photo selection
         */
        const val PICK_PHOTO_REQUEST_CODE = 2

        /**
         * Request code for taking photo
         */
        const val TAKE_PHOTO_REQUEST_CODE = 3

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
     * Intent for taking photo
     */
    private val takePhotoIntent by lazy { Intent(MediaStore.ACTION_IMAGE_CAPTURE) }

    private lateinit var newNoteFragment: NewNoteFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)
        // Try to restore fragment
        newNoteFragment = restoreFragment() as NewNoteFragment? ?: NewNoteFragment()
        setFragment(newNoteFragment)
    }

    override fun speak(): Unit = startSpeechRecognition()

    override fun pickImageFromGallery() {
        if (checkIntentHandlers(pickPhotoFromGalleryIntent)) {
            startActivityForResult(pickPhotoFromGalleryIntent, PICK_PHOTO_REQUEST_CODE)
        }
    }

    override fun takePhoto() {
        if (checkIntentHandlers(takePhotoIntent)) {
            val photoFile = createImageFile(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES))
            newNoteFragment.photoUri = FileProvider.getUriForFile(
                    this, FILE_PROVIDER_AUTHORITY, photoFile)
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, newNoteFragment.photoUri)
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST_CODE)
        }
    }

    override fun cropImage(photoUri: Uri?) {
        if (photoUri != null) {
            showCropActivity(photoUri)
        } else {
            showToast(R.string.image_loading_error)
        }
    }

    override fun onCreationSuccess() {
        showToast(R.string.note_created)
        setResult(Activity.RESULT_OK)
        finish()
    }

    /**
     * Function for cropping image of note which is using Uri from the NewNoteFragment
     */
    private fun cropFragmentImage(): Unit = cropImage(newNoteFragment.photoUri)

    /**
     * Function for receiving an activity's result
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        isRecognitionServiceStarted = false
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                // Result of a speech recognition
                SPEECH_REQUEST_CODE -> {
                    if (data != null) {
                        val resultArray =
                                data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                        newNoteFragment.setText(resultArray[0].capitalize())
                    }
                }

                // Result of a choosing photo from gallery
                PICK_PHOTO_REQUEST_CODE -> {
                    // If request code is equal to request code for pick photo from gallery,
                    // we should set photoUri, otherwise photoUri had been already set
                    newNoteFragment.photoUri = data?.data
                    cropFragmentImage()
                }

                // Result of taking photo
                TAKE_PHOTO_REQUEST_CODE -> cropFragmentImage()

                // Result of image cropping
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ->
                    newNoteFragment.cropImage(CropImage.getActivityResult(data).uri)
            }
        } // if
    } // fun
}
