package com.margarita.voicenotes.mvp.presenter

import com.margarita.voicenotes.models.entities.NoteItem
import com.margarita.voicenotes.mvp.view.BaseView
import com.margarita.voicenotes.mvp.view.NotesView
import io.realm.Realm
import io.realm.RealmQuery

/**
 * Presenter for getting a list of notes and show it in the view
 */
class NotesPresenter(view: BaseView<NoteItem>): BasePresenter<NoteItem>(view) {

    override fun performQuery(realm: Realm): RealmQuery<NoteItem>
            = realm.where(NoteItem::class.java)
}