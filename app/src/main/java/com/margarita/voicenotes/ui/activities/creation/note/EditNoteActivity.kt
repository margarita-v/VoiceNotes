package com.margarita.voicenotes.ui.activities.creation.note

import android.os.Bundle
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.parseToString
import com.margarita.voicenotes.models.entities.NoteItem
import com.margarita.voicenotes.ui.fragments.dialogs.confirm.CancelEditDialogFragment

/**
 * Activity for edit of notes
 */
class EditNoteActivity : NewNoteActivity(), CancelEditDialogFragment.CancelEditListener {

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
        // Check if the note fields were changed
        if (noteForEdit.isDifferentFrom(
                        description = newNoteFragment.getDescription(),
                        categoryId = newNoteFragment.getCategoryId(),
                        photoUri = newNoteFragment.photoUri?.parseToString(),
                        croppedPhotoUri = newNoteFragment.croppedPhotoUri?.parseToString())) {
            showCancelDialog()
        } else {
            finish()
        }
    }

    override fun save() {
        val oldUri = noteForEdit.getPhotoUri()

        // Check if the photo was saved in the internal storage
        if (oldUri != null
                && newNoteFragment.photoUri != oldUri
                && oldUri.toString().startsWith(COMMON_URI_STRING)) {
            // User replaced an old photo. We should check if the note has description
            if (newNoteFragment.photoUri != null || newNoteFragment.getDescription() != "") {
                contentResolver.delete(oldUri, null, null)
                newNoteFragment.save()
            } else {
                newNoteFragment.showError(R.string.note_empty)
            }
        } else {
            newNoteFragment.save()
        }
    }

    override fun cancelSaving() {
        // Delete a new photo
        deletePhotoFile(newPhotoFile)

        // Delete an old saved photo, if the photo was changed
        if (newNoteFragment.photoFile != null &&
                noteForEdit.getPhotoUri() != newNoteFragment.photoUri) {
            deletePhotoFile(newNoteFragment.photoFile)
        }
        finish()
    }

    override fun usedForCreation() = false

    override fun getExitMessageResId() = R.string.confirm_cancel_edit
}
