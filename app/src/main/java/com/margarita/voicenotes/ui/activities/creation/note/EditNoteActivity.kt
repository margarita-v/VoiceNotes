package com.margarita.voicenotes.ui.activities.creation.note

import android.os.Bundle
import com.margarita.voicenotes.R
import com.margarita.voicenotes.models.entities.NoteItem

/**
 * Activity for edit of notes
 */
class EditNoteActivity : NewNoteActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Show note's info
        val noteItem = getParcelableExtra(R.string.note_intent) as NoteItem
        newNoteFragment.showNoteInfo(noteItem)
    }

    override fun usedForCreation(): Boolean = false
}
