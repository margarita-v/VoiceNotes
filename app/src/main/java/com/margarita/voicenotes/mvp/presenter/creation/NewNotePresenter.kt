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
     * @param categoryId ID of category for the note
     */
    fun createNote(description: String,
                   date: Long,
                   photoUri: Uri? = null,
                   croppedPhotoUri: Uri? = null,
                   categoryId: Long? = null) {
        if (description.isNotEmpty()) {
            realm.executeTransaction { realm1 ->
                val noteItem = NoteItem(
                        generateId(),
                        description,
                        date,
                        photoUri?.parseToString(),
                        croppedPhotoUri?.parseToString())
                realm1.copyToRealm(noteItem)
                // Set category to the new note
                setCategoryOfNote(realm1, noteItem, categoryId)
            }
            view.onCreationSuccess()
        } else {
            view.showError(R.string.note_empty)
        }
    }

    /**
     * Function for the note editing
     * @param id ID of note which will be edited
     * @param description A text of note
     * @param photoUri Photo of note
     * @param croppedPhotoUri Cropped photo for a note's thumbnail
     * @param categoryId ID of category of the note
     */
    fun editNote(id: Long,
                 description: String,
                 photoUri: Uri? = null,
                 croppedPhotoUri: Uri? = null,
                 categoryId: Long? = null) {
        if (description.isNotEmpty()) {
            realm.executeTransaction { realm1 ->
                // Find existing note item
                val noteItem = realm1.where(NoteItem::class.java)
                        .equalTo(ID_FIELD, id)
                        .findFirst()
                if (noteItem != null) {
                    noteItem.description = description
                    noteItem.photoUri = photoUri?.parseToString()
                    noteItem.croppedPhotoUri = croppedPhotoUri?.parseToString()
                    realm1.copyToRealmOrUpdate(noteItem)
                    setCategoryOfNote(realm1, noteItem, categoryId)
                }
            }
            view.onCreationSuccess()
        } else {
            view.showError(R.string.note_empty)
        }
    }

    /**
     * Function for setting category for the note
     * @param realm Realm instance for database access
     * @param noteItem Note which category will be set
     * @param categoryId ID of category for the note
     */
    private fun setCategoryOfNote(realm: Realm, noteItem: NoteItem, categoryId: Long?) {
        if (categoryId != null) {
            val category = performQuery(realm)
                    .equalTo(ID_FIELD, categoryId)
                    .findFirst()
            if (category != null && !category.containNote(noteItem)) {
                category.notes.add(noteItem)
                realm.copyToRealmOrUpdate(category)
            }
        }
    }
}