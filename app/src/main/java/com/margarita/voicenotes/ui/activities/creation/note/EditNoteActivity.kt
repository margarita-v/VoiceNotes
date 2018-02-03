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
        newNoteFragment.noteForEdit = getParcelableExtra(R.string.note_intent) as NoteItem
    }

    override fun usedForCreation(): Boolean = false

    override fun onEditSuccess(messageRes: Int): Unit = finishAction(messageRes)
}
