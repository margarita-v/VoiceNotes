package com.margarita.voicenotes.ui.activities.creation.note

import android.os.Bundle

/**
 * Activity for edit of notes
 */
class EditNoteActivity : NewNoteActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO Show note's info
    }

    override fun usedForCreation(): Boolean = false
}
