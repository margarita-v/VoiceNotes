package com.margarita.voicenotes.mvp.presenter

import android.net.Uri
import com.margarita.voicenotes.mvp.view.NotesView
import io.realm.Realm
import io.realm.RealmObject

class NotesPresenter(private val view: NotesView) {

    private lateinit var realm: Realm

    init {
        realm = Realm.getDefaultInstance()
    }

    fun loadNotes() {

    }

    fun loadCategory() {

    }

    fun saveCategory(name: String) {

    }

    fun saveNoteItem(description: String,
                     date: Long,
                     photoUri: Uri? = null,
                     croppedPhotoUri: Uri? = null) {

    }

    fun removeCategory(id: Int) {

    }

    fun removeNoteItem(id: Int) {

    }

    fun update(realmObject: RealmObject) {

    }
}