package com.margarita.voicenotes.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.TextView
import com.margarita.voicenotes.R
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        if (intent != null) {
            val resultArray = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val speechResult = resultArray[0]
            etNote.setText(speechResult, TextView.BufferType.EDITABLE)
            etNote.setSelection(speechResult.length)
        }
    }
}
