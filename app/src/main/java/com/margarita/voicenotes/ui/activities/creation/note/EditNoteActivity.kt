package com.margarita.voicenotes.ui.activities.creation.note

import android.os.Bundle
import com.margarita.voicenotes.R
import com.margarita.voicenotes.models.entities.NoteItem

/**
 * Activity for edit of notes
 */
class EditNoteActivity : NewNoteActivity() {

    /**
     * Note which will be edited
     */
    private lateinit var noteForEdit: NoteItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteForEdit = getParcelableExtra(R.string.note_intent) as NoteItem

        // Show note's info
        newNoteFragment.noteForEdit = noteForEdit
    }

    override fun onBackPressed() {
        finish()
    }

    override fun usedForCreation(): Boolean = false

    override fun getExitMessageResId(): Int = R.string.confirm_cancel_note_edit
}
