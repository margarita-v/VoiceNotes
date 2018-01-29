package com.margarita.voicenotes.mvp.presenter.list

import com.margarita.voicenotes.models.entities.NoteItem
import com.margarita.voicenotes.mvp.view.BaseView
import io.realm.Realm
import io.realm.RealmQuery

/**
 * Presenter for getting a list of notes and show it in the view
 */
class NotesPresenter(view: BaseView<NoteItem>): BaseListPresenter<NoteItem>(view) {

    override fun performQuery(realm: Realm): RealmQuery<NoteItem>
            = realm.where(NoteItem::class.java)
}