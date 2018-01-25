package com.margarita.voicenotes.mvp.presenter

import android.net.Uri
import com.margarita.voicenotes.models.entities.NoteItem
import com.margarita.voicenotes.mvp.view.NewNoteView

/**
 * Presenter for creating a new notes
 */
class NewNotePresenter(view: NewNoteView): CategoriesPresenter(view) {

    /**
     * Function for creation a new note with the given fields
     * @param description A text of note
     * @param date Note's date
     * @param photoUri Photo of note
     * @param croppedPhotoUri Cropped photo for a note's thumbnail
     */
    fun createNote(description: String,
                       date: Long,
                       photoUri: Uri? = null,
                       croppedPhotoUri: Uri? = null) {
        val noteItem = NoteItem(
                generateId(),
                description,
                date,
                photoUri.toString(),
                croppedPhotoUri.toString())
        save(noteItem)
    }
}