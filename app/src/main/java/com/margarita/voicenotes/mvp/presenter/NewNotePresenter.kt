package com.margarita.voicenotes.mvp.presenter

import android.net.Uri
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.parseToString
import com.margarita.voicenotes.models.entities.NoteItem
import com.margarita.voicenotes.mvp.view.NewNoteView

/**
 * Presenter for creating a new notes
 */
class NewNotePresenter(private val view: NewNoteView): CategoriesPresenter(view) {

    /**
     * Generate ID for a new note
     */
    override fun generateId(): Long
        = realm
            .where(NoteItem::class.java)
            .max(ID_FIELD)
            ?.toLong()
            ?.plus(1) ?: 1

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
        val id = generateId()
        if (!description.isEmpty()) {
            val noteItem = NoteItem(
                    id,
                    description,
                    date,
                    photoUri?.parseToString(),
                    croppedPhotoUri?.parseToString())
            save(noteItem)
            view.onCreationSuccess()
        } else {
            view.showError(R.string.note_empty)
        }
    }
}