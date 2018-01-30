package com.margarita.voicenotes.mvp.presenter.list

import com.margarita.voicenotes.models.entities.Category
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

    override fun performQuery(realm: Realm): RealmQuery<Category>
            = realm.where(Category::class.java)

    override fun getSortField(): String = SORT_FIELD

    override fun getSortOrder(): Sort = Sort.ASCENDING
}