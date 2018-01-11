package com.margarita.voicenotes.ui.activities

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.TextView
import com.margarita.voicenotes.R
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
        const val SPEECH_REQUEST_CODE = 100
    }

    /**
     * Flag which shows if the recognition service was started
     */
    private var isRecognitionServiceStarted = false

    /**
     * Flag which shows if the screeen orientation was changed
     */
    private var isScreenOrientationChanged = false

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
     * Function for launching speech recognition service
     */
    private fun startSpeechRecognition() {
        try {
            if (!isRecognitionServiceStarted && !isScreenOrientationChanged) {
                startActivityForResult(recognitionIntent, SPEECH_REQUEST_CODE)
                isRecognitionServiceStarted = true
            }
        } catch (a: ActivityNotFoundException) {
            showToast(R.string.speech_not_supported)
        }
    }

    /**
     * Function for receiving speech input
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        isRecognitionServiceStarted = false
        if (requestCode == SPEECH_REQUEST_CODE &&
                resultCode == Activity.RESULT_OK && data != null) {
            val resultArray = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val speechResult = resultArray[0]
            etNote.setText(speechResult, TextView.BufferType.EDITABLE)
            etNote.setSelection(speechResult.length)
        }
    }
}
