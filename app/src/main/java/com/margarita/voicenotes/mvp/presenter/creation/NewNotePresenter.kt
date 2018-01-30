package com.margarita.voicenotes.mvp.presenter.creation

import android.net.Uri
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.parseToString
import com.margarita.voicenotes.models.entities.Category
import com.margarita.voicenotes.models.entities.NoteItem
import com.margarita.voicenotes.mvp.presenter.base.BaseDatabasePresenter
import com.margarita.voicenotes.mvp.view.NewNoteView
import io.realm.Realm
import io.realm.RealmQuery

/**
 * Presenter for creating a new notes
 */
class NewNotePresenter(private val view: NewNoteView)
    : BaseDatabasePresenter<Category>() {

    companion object {
        private const val SORT_FIELD = "name"
    }

    override fun performQuery(realm: Realm): RealmQuery<Category>
            = realm.where(Category::class.java)

    override fun getSortField(): String = SORT_FIELD

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
     * Function for loading a list of categories
     */
    fun loadCategories() {
        view.showLoading()
        val items = getData()
        view.hideLoading()
        view.setCategories(items)
    }

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
        if (!description.isEmpty()) {
            val noteItem = NoteItem(
                    generateId(),
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