package com.margarita.voicenotes.ui.activities.creation.note

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.annotation.StringRes
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.parseStringToUri
import com.margarita.voicenotes.common.parseToString
import com.margarita.voicenotes.common.throwClassCastException
import com.margarita.voicenotes.models.entities.NoteItem
import com.margarita.voicenotes.ui.fragments.dialogs.ConfirmDialogFragment

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
                        newNoteFragment.getDescription(),
                        newNoteFragment.getCategoryId(),
                        newNoteFragment.photoUri?.parseToString(),
                        newNoteFragment.croppedPhotoUri?.parseToString())) {
            CancelEditDialogFragment
                    .newInstance(R.string.confirm_cancel_note_edit)
                    .show(supportFragmentManager, CancelEditDialogFragment.SHOWING_TAG)
        } else {
            finish()
        }
    }

    override fun save() {
        val oldUri = noteForEdit.photoUri?.parseStringToUri()

        // Check if the photo was saved in the internal storage
        if (oldUri != null
                && newNoteFragment.photoUri != oldUri
                && oldUri.toString().startsWith(COMMON_URI_STRING)) {
            // User replaced an old photo. We should check if the note has description
            if (newNoteFragment.photoUri != null || newNoteFragment.getDescription() != "") {
                contentResolver.delete(oldUri, null, null)
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
        finish()
    }

    override fun usedForCreation() = false

    override fun getExitMessageResId() = R.string.confirm_cancel_note_edit
}

/**
 * Class for showing a confirmation dialog for cancel of editing note
 */
class CancelEditDialogFragment: ConfirmDialogFragment() {

    /**
     * Callback to calling activity or fragment
     */
    private lateinit var cancelEditListener: CancelEditListener

    companion object {

        /**
         * Tag for a dialog showing
         */
        const val SHOWING_TAG = ConfirmDialogFragment.SHOWING_TAG

        /**
         * Function for creation an instance of dialog
         */
        fun newInstance(@StringRes messageRes: Int): CancelEditDialogFragment
                = ConfirmDialogFragment.configureDialog(CancelEditDialogFragment(), messageRes)
                    as CancelEditDialogFragment
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            cancelEditListener = context as CancelEditListener
        } catch (e: ClassCastException) {
            throwClassCastException(context, "CancelEditListener")
        }
    }

    override fun onClick(dialogInterface: DialogInterface?, i: Int) {
        when (i) {
            Dialog.BUTTON_NEGATIVE -> cancelEditListener.cancelSaving()
            Dialog.BUTTON_POSITIVE -> cancelEditListener.save()
        }
    }

    /**
     * Interface for callback implementation
     */
    interface CancelEditListener {

        /**
         * Function for saving changes
         */
        fun save()

        /**
         * Function for canceling of saving changes
         */
        fun cancelSaving()
    }
}
