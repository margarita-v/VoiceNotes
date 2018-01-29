package com.margarita.voicenotes.mvp.presenter.creation

import com.margarita.voicenotes.R
import com.margarita.voicenotes.models.entities.Category
import com.margarita.voicenotes.mvp.presenter.base.BaseDatabasePresenter
import com.margarita.voicenotes.mvp.view.NewCategoryView
import io.realm.Realm
import io.realm.RealmQuery

/**
 * Presenter for creating a new categories
 */
class NewCategoryPresenter(private val view: NewCategoryView)
    : BaseDatabasePresenter<Category>() {

    companion object {
        private const val NAME_FIELD = "name"
    }

    override fun performQuery(realm: Realm): RealmQuery<Category>
            = realm.where(Category::class.java)

    /**
     * Function for creation a new category with the given name
     * @param name Name of category
     */
    fun createCategory(name: String) {
        if (!name.isEmpty()) {
            // Check if a category with the same name exists
            view.showLoading()
            val realmResults = performQuery(realm)
                    .equalTo(NAME_FIELD, name)
                    .findAll()
            view.hideLoading()

            if (realmResults.isEmpty()) {
                save(Category(generateId(), name))
                view.onCreationSuccess()
            } else {
                view.showError(R.string.category_exists)
            }
        } else {
            view.showError(R.string.category_empty)
        }
    }
}