package com.margarita.voicenotes.ui.activities

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.content.ActivityNotFoundException
import android.speech.RecognizerIntent
import android.content.Intent
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.showToast
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    /**
     * Request code for recognition activity
     */
    private val requestCodeForSpeech = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        imgBtnSpeak.setOnClickListener { startSpeechRecognition() }
    }

    /**
     * Function for launching speech recognition service
     */
    private fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault())
        intent.putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.start_speech))
        try {
            startActivityForResult(intent, requestCodeForSpeech)
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
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            tvSpeechResult.text = result[0]
        }
    }
}
