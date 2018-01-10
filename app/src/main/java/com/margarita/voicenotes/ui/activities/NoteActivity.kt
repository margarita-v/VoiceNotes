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

class NoteActivity : AppCompatActivity() {

    /**
     * Request code for recognition activity
     */
    private val requestCodeForSpeech = 100

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
        //TODO Add launch flag and save it to Bundle
        //TODO Add function which will check flag and launch service
        startSpeechRecognition()
        imgBtnSpeak.setOnClickListener { startSpeechRecognition() }
    }

    /**
     * Function for launching speech recognition service
     */
    private fun startSpeechRecognition() {
        try {
            startActivityForResult(recognitionIntent, requestCodeForSpeech)
        } catch (a: ActivityNotFoundException) {
            showToast(R.string.speech_not_supported)
        }
    }

    /**
     * Function for receiving speech input
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodeForSpeech &&
                resultCode == Activity.RESULT_OK && data != null) {
            val resultArray = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val speechResult = resultArray[0]
            etNote.setText(speechResult, TextView.BufferType.EDITABLE)
            etNote.setSelection(speechResult.length)
        }
    }
}
