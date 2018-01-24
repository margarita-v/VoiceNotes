package com.margarita.voicenotes.mvp.presenter

import android.net.Uri
import com.margarita.voicenotes.models.entities.Category
import com.margarita.voicenotes.models.entities.NoteItem
import com.margarita.voicenotes.mvp.view.NotesView
import io.realm.Realm
import io.realm.RealmObject
import io.realm.Sort

class NotesPresenter(private val view: NotesView) {

    /**
     * Realm instance for working with the local database
     */
    private var realm: Realm = Realm.getDefaultInstance()

    companion object {
        /**
         * Names of fields
         */
        private const val ID_FIELD = "id"
        private const val DATE_FIELD = "date"
        private const val NAME_FIELD = "name"
    }

    /**
     * Function for loading all notes from the local database
     */
    fun loadNotes() {
        val realmResults = realm.where(NoteItem::class.java)
                .sort(DATE_FIELD, Sort.DESCENDING)
                .findAll()

        val notes = realm.copyFromRealm(realmResults)
        if (notes.isEmpty()) {
            view.showEmptyView()
        } else {
            view.setNotes(notes)
        }
    }

    /**
     * Function for loading all categories from the local database
     */
    fun loadCategories() {
        val realmResults = realm.where(Category::class.java)
                .sort(NAME_FIELD, Sort.DESCENDING)
                .findAll()

        val categories = realm.copyFromRealm(realmResults)
        if (categories.isEmpty()) {
            view.showEmptyView()
        } else {

        }
    }

    /**
     * Function for creation a new category with the given name
     * @param name Name of category
     */
    fun createCategory(name: String) {

    }

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

    /**
     * Function for removing a category with the given ID
     * @param id ID of category which will be removed
     */
    fun removeCategory(id: Int) {

    }

    /**
     * Function for removing a note with the given ID
     * @param id ID of note which will be removed
     */
    fun removeNoteItem(id: Int) {

    }

    /**
     * Function for saving an object to the local database
     * @param realmObject Object which will be saved to the local database
     */
    fun update(realmObject: RealmObject): Unit
            = realm.executeTransaction { realm1 -> realm1.copyToRealmOrUpdate(realmObject) }

    /**
     * Function for ID generation for a new object
     * @param isForNote Flag which shows if the generated ID will set to the note item
     */
    private fun generateId(isForNote: Boolean): Long
            = realm
                .where(if (isForNote) NoteItem::class.java else Category::class.java)
                .max(ID_FIELD)
                ?.toLong()
                ?.plus(1)
                ?: 1

}