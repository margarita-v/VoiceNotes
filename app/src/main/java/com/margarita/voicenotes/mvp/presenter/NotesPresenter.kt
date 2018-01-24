package com.margarita.voicenotes.mvp.presenter

import android.net.Uri
import com.margarita.voicenotes.models.entities.NoteItem
import com.margarita.voicenotes.mvp.view.NotesView
import io.realm.RealmQuery

class NotesPresenter(view: NotesView): BasePresenter<NoteItem>(view) {

    override fun performQuery(): RealmQuery<NoteItem> = realm.where(NoteItem::class.java)

    /**
     * Function for creation a new note with the given fields
     * @param description A text of note
     * @param date Note's date
     * @param photoUri Photo of note
     * @param croppedPhotoUri Cropped photo for a note's thumbnail
     */
    fun createNoteItem(description: String,
                     date: Long,
                     photoUri: Uri? = null,
                     croppedPhotoUri: Uri? = null) {

    }
}