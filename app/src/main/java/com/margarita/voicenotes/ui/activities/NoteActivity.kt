package com.margarita.voicenotes.ui.activities

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.support.v4.content.FileProvider
import android.widget.TextView
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.createImageFile
import com.margarita.voicenotes.common.loadImage
import com.margarita.voicenotes.common.showToast
import kotlinx.android.synthetic.main.activity_note.*
import java.util.*

/**
 * Activity for a note creation, showing and editing
 */
class NoteActivity : AppCompatActivity() {

    /**
     * Storage of static constants
     */
    private companion object {
        /**
         * Authority for the application's file provider
         */
        const val FILE_PROVIDER_AUTHORITY = "com.margarita.voicenotes.android.fileprovider"

        /**
         * Key for Bundle for saving a flag for started recognition service
         */
        const val RECOGNITION_SERVICE_FLAG = "START_RECOGNITION"

        /**
         * Key for Bundle for saving a flag for screen orientation changing
         */
        const val SCREEN_ORIENTATION_CHANGED_FLAG = "ROTATED"

        /**
         * Request code for recognition service
         */
        const val SPEECH_REQUEST_CODE = 1

        /**
         * Request code for a photo selection
         */
        const val PICK_PHOTO_REQUEST_CODE = 2

        /**
         * Request code for taking photo
         */
        const val TAKE_PHOTO_REQUEST_CODE = 3
    }

    /**
     * Flag which shows if the recognition service was started
     */
    private var isRecognitionServiceStarted = false

    /**
     * Flag which shows if the screen orientation was changed
     */
    private var isScreenOrientationChanged = false

    /**
     * Uri for the photo of note
     */
    private var photoUri: Uri? = null

    /**
     * Intent for starting speech recognition service
     */
    private val recognitionIntent by lazy {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                .putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                .putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault())
                .putExtra(
                        RecognizerIntent.EXTRA_PROMPT,
                        getString(R.string.start_speech))
    }

    /**
     * Intent for choosing photo from gallery
     */
    private val pickPhotoFromGalleryIntent by lazy {
        val intentType = "image/*"
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                .setType(intentType)
    }

    /**
     * Intent for taking photo
     */
    private val takePhotoIntent by lazy {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        if (savedInstanceState != null) {
            isRecognitionServiceStarted =
                    savedInstanceState.getBoolean(RECOGNITION_SERVICE_FLAG)
            isScreenOrientationChanged =
                    savedInstanceState.getBoolean(SCREEN_ORIENTATION_CHANGED_FLAG)
        }
        startSpeechRecognition()
        // This flag used once on activity creation, so we should set it to false
        isScreenOrientationChanged = false
        imgBtnSpeak.setOnClickListener { startSpeechRecognition() }
        imgBtnPhoto.setOnClickListener { takePhoto() }
        imgBtnChoosePhoto.setOnClickListener { pickImageFromGallery() }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(RECOGNITION_SERVICE_FLAG, isRecognitionServiceStarted)
        // This method is always called when screen orientation is changing
        outState?.putBoolean(SCREEN_ORIENTATION_CHANGED_FLAG, true)
    }

    /**
     * Providing a correct navigation to the parent activity
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * Function for choosing a photo for note from gallery
     */
    private fun pickImageFromGallery() {
        if (checkIntentHandlers(pickPhotoFromGalleryIntent)) {
            startActivityForResult(pickPhotoFromGalleryIntent, PICK_PHOTO_REQUEST_CODE)
        }
    }

    /**
     * Function for taking photo for note
     */
    private fun takePhoto() {
        if (checkIntentHandlers(takePhotoIntent)) {
            val photoFile = createImageFile(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES))
            photoUri = FileProvider.getUriForFile(
                    this, FILE_PROVIDER_AUTHORITY, photoFile)
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST_CODE)
        }
    }

    /**
     * Function for launching speech recognition service
     */
    private fun startSpeechRecognition() {
        try {
            if (!isRecognitionServiceStarted && !isScreenOrientationChanged &&
                    checkIntentHandlers(recognitionIntent)) {
                startActivityForResult(recognitionIntent, SPEECH_REQUEST_CODE)
                isRecognitionServiceStarted = true
            }
        } catch (a: ActivityNotFoundException) {
            showToast(R.string.speech_not_supported)
        }
    }

    /**
     * Function which checks if the user's device has apps which can handle intent
     * @param intent Intent which should be handled
     * @return True if intent could be handled, False otherwise
     */
    private fun checkIntentHandlers(intent: Intent) =
            intent.resolveActivity(packageManager) != null

    /**
     * Function for receiving an activity's result
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        isRecognitionServiceStarted = false
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                // Result of a speech recognition
                SPEECH_REQUEST_CODE -> {
                    val resultArray =
                            data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    val speechResult = resultArray[0].capitalize()
                    // Capitalize the first letter of note
                    etNote.setText(speechResult, TextView.BufferType.EDITABLE)
                    etNote.setSelection(speechResult.length)
                }
                else -> {
                    if (requestCode == PICK_PHOTO_REQUEST_CODE) {
                        photoUri = data.data
                    }
                    ivPhoto.loadImage(this, photoUri!!)
                }
            } // when
        } // if
    } // fun
}
