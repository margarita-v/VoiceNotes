package com.margarita.voicenotes.mvp.presenter

import com.margarita.voicenotes.models.entities.Category
import com.margarita.voicenotes.mvp.view.BaseView
import io.realm.Realm
import io.realm.RealmQuery

/**
 * Presenter for getting a list of categories and creating new categories
 */
open class CategoriesPresenter(view: BaseView<Category>) : BasePresenter<Category>(view) {

    companion object {
        private const val SORT_FIELD = "name"
    }

    override fun performQuery(realm: Realm): RealmQuery<Category>
            = realm.where(Category::class.java)

    override fun getSortField(): String = SORT_FIELD

    /**
     * Function for creation a new category with the given name
     * @param name Name of category
     */
    fun createCategory(name: String): Unit
            = save(Category(generateId(), name))
}