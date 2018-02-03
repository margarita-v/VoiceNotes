package com.margarita.voicenotes.ui.activities.creation

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.annotation.StringRes
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.showToast
import com.margarita.voicenotes.ui.activities.BaseActivity
import com.margarita.voicenotes.ui.fragments.creation.BaseNewItemFragment
import java.util.*

/**
 * Base activity for creation of items
 */
abstract class BaseNewItemActivity(@StringRes private val speechMessageRes: Int) :
        BaseActivity(),
        BaseNewItemFragment.BaseSelectedOption {

    companion object {

        /**
         * Key for Bundle for saving a flag for started recognition service
         */
        private const val RECOGNITION_SERVICE_FLAG = "START_RECOGNITION"

        /**
         * Key for Bundle for saving a flag for screen orientation changing
         */
        private const val SCREEN_ORIENTATION_CHANGED_FLAG = "ROTATED"

        /**
         * Request code for recognition service
         */
        const val SPEECH_REQUEST_CODE = 1
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
                        getString(speechMessageRes))
    }

    /**
     * Current visible fragment
     */
    protected lateinit var fragment: BaseNewItemFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        // Restore flags for recognition service launching
        if (savedInstanceState != null) {
            isRecognitionServiceStarted =
                    savedInstanceState.getBoolean(RECOGNITION_SERVICE_FLAG)
            isScreenOrientationChanged =
                    savedInstanceState.getBoolean(SCREEN_ORIENTATION_CHANGED_FLAG)
        }

        if (usedForCreation()) {
            startSpeechRecognition()
        }

        // This flag used once on activity creation, so we should set it to false
        isScreenOrientationChanged = false
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(RECOGNITION_SERVICE_FLAG, isRecognitionServiceStarted)
        // This method is always called when screen orientation is changing
        outState?.putBoolean(SCREEN_ORIENTATION_CHANGED_FLAG, true)
    }

    override fun speak(): Unit = startSpeechRecognition()

    override fun onCreationSuccess(messageRes: Int) {
        showToast(messageRes)
        setResult(Activity.RESULT_OK)
        finish()
    }

    /**
     * Function for launching speech recognition service
     */
    private fun startSpeechRecognition() {
        try {
            if (!isRecognitionServiceStarted && !isScreenOrientationChanged
                    && checkIntentHandlers(recognitionIntent)) {
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
    protected fun checkIntentHandlers(intent: Intent): Boolean
            = intent.resolveActivity(packageManager) != null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        isRecognitionServiceStarted = false
        if (resultCode == Activity.RESULT_OK &&
                requestCode == SPEECH_REQUEST_CODE && data != null) {
            // Result of speech recognition
            val resultArray = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            fragment.setText(resultArray[0].capitalize())
        }
    }

    /**
     * Function which returns true if the activity is used for creation of items,
     * false otherwise.
     * If the activity is not used for creation, we don't need to launch
     * a speech recognition service when activity is created.
     */
    protected abstract fun usedForCreation(): Boolean
}