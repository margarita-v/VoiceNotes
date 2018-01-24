package com.margarita.voicenotes.mvp.presenter

import com.margarita.voicenotes.models.entities.Category
import com.margarita.voicenotes.mvp.view.CategoriesView
import io.realm.RealmQuery

class CategoriesPresenter(view: CategoriesView) : BasePresenter<Category>(view) {

    companion object {
        private const val SORT_FIELD = "name"
    }

    override fun performQuery(): RealmQuery<Category> = realm.where(Category::class.java)

    override fun getSortField(): String = SORT_FIELD

    /**
     * Function for creation a new category with the given name
     * @param name Name of category
     */
    fun createCategory(name: String) {

    }
}