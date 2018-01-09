package com.margarita.voicenotes.ui.activities

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.content.ActivityNotFoundException
import android.speech.RecognizerIntent
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.NotesAdapter
import com.margarita.voicenotes.common.showToast
import com.margarita.voicenotes.models.NoteItem
import kotlinx.android.synthetic.main.fragment_news.*
import java.util.*

class MainActivity : AppCompatActivity() {

    /**
     * Request code for recognition activity
     */
    private val requestCodeForSpeech = 100

    /**
     * Lazy initialization for RecyclerView which will be executed once
     */
    private val notesList by lazy {
        rvList.setHasFixedSize(true)
        rvList.layoutManager = LinearLayoutManager(this)
        rvList
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupAdapter()
        fab.setOnClickListener { startSpeechRecognition() }
    }

    /**
     * Function for initialization of the RecyclerView's adapter
     */
    private fun setupAdapter() {
        val adapter = NotesAdapter()
        val notes = (1..10).map {
            NoteItem(
                    it,
                    "$it A very long text of note. What's up? How are you? I am writing a useful app for you! And what are you doing?",
                    1457207701L - it * 200
            )
        }
        adapter.setNotes(notes)
        notesList.adapter = adapter
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
            //val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            //tvSpeechResult.text = result[0]
        }
    }
}
