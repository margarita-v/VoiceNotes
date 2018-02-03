package com.margarita.voicenotes.mvp.presenter.creation

import com.margarita.voicenotes.R
import com.margarita.voicenotes.models.entities.Category
import com.margarita.voicenotes.mvp.presenter.base.BaseDatabasePresenter
import com.margarita.voicenotes.mvp.view.BaseNewItemView
import io.realm.Realm
import io.realm.RealmQuery

/**
 * Presenter for creating a new categories
 */
class NewCategoryPresenter(private val view: BaseNewItemView)
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
        val nameTrimmed = name.trim()
        if (nameTrimmed.isNotEmpty()) {
            realm.executeTransaction { realm1 ->
                // Check if the category with the same name exists
                view.showLoading()
                val realmResults = performQuery(realm1)
                        .equalTo(NAME_FIELD, nameTrimmed)
                        .findAll()
                view.hideLoading()

                if (realmResults.isEmpty()) {
                    save(realm1, Category(generateId(), nameTrimmed))
                    view.onCreationSuccess()
                } else {
                    view.showError(R.string.category_exists)
                }
            }
        } else {
            view.showError(R.string.category_empty)
        }
    }

    /**
     * Function for the category editing
     * @param id ID of category which will be edited
     * @param name A new name of category
     */
    fun editCategory(id: Long, name: String) {
        val nameTrimmed = name.trim()
        if (nameTrimmed.isNotEmpty()) {
            realm.executeTransaction { realm1 ->
                view.showLoading()
                val category = performQuery(realm1)
                        .equalTo(ID_FIELD, id)
                        .findFirst()
                if (category != null && category.name != nameTrimmed) {
                    // Check if the category with the same name exists
                    val realmResults = performQuery(realm1)
                            .equalTo(NAME_FIELD, nameTrimmed)
                            .findAll()
                    view.hideLoading()
                    if (realmResults.isEmpty()) {
                        category.name = nameTrimmed
                        save(realm1, category)
                        view.onEditSuccess()
                    } else {
                        view.showError(R.string.category_exists)
                    }
                }
            }
        } else {
            view.showError(R.string.category_empty)
        }
    }
}