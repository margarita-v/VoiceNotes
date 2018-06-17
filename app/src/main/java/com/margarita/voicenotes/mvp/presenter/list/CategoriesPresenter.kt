package com.margarita.voicenotes.mvp.presenter.list

import com.margarita.voicenotes.models.entities.Category
import com.margarita.voicenotes.models.entities.NoteItem
import com.margarita.voicenotes.mvp.view.BaseView
import io.realm.Realm
import io.realm.RealmQuery
import io.realm.Sort

/**
 * Presenter for getting a list of categories and creating new categories
 */
class CategoriesPresenter(view: BaseView<Category>) : BaseListPresenter<Category>(view) {

    companion object {
        private const val SORT_FIELD = "name"
    }

    override fun removeByIds(ids: Set<Long>) {
        ids.forEach {
            val categoryId = it
            realm.executeTransaction { realm1 ->
                val realmNotes = realm1.where(NoteItem::class.java)
                        .findAll()
                val notes = realm1.copyFromRealm(realmNotes)
                notes.forEach {
                    if (getCategory(it.id)?.id == categoryId) {
                        realm1.where(NoteItem::class.java)
                                .equalTo(ID_FIELD, it.id)
                                .findAll()
                                .deleteAllFromRealm()
                    }
                }
            }
        }
        super.removeByIds(ids)
    }

    override fun performQuery(realm: Realm): RealmQuery<Category>
            = realm.where(Category::class.java)

    override fun getSortField(): String = SORT_FIELD

    override fun getSortOrder(): Sort = Sort.ASCENDING
}