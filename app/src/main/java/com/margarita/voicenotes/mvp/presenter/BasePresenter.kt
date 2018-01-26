package com.margarita.voicenotes.mvp.presenter

import com.margarita.voicenotes.mvp.view.BaseView
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmQuery
import io.realm.Sort

/**
 * Base class for all presenters
 * @param T A type of items which will be shown by presenter
 */
abstract class BasePresenter<T: RealmObject>(private val view: BaseView<T>) {

    /**
     * Realm instance for working with the local database
     */
    protected var realm: Realm = Realm.getDefaultInstance()

    companion object {
        const val ID_FIELD = "id"
        private const val SORT_FIELD = "date"
    }

    /**
     * Function for loading all items of the concrete type from the local database
     */
    fun loadItems() {
        view.showLoading()
        val realmResults = performQuery(realm)
                .sort(getSortField(), Sort.DESCENDING)
                .findAll()

        val items = realm.copyFromRealm(realmResults)
        view.hideLoading()

        if (items.isEmpty()) {
            view.showEmptyView()
        } else {
            view.setItems(items)
        }
    }

    /**
     * Function for removing an item with the given ID
     * @param id ID of item which will be removed
     */
    fun remove(id: Int) {
        realm.executeTransaction { realm1 ->
            performQuery(realm1)
                    .equalTo(ID_FIELD, id)
                    .findAll()
                    .deleteAllFromRealm()
        }
    }

    /**
     * Function for saving an object to the local database
     * @param realmObject Object which will be saved to the local database
     */
    fun save(realmObject: RealmObject): Unit
            = realm.executeTransaction { realm1 -> realm1.copyToRealmOrUpdate(realmObject) }

    /**
     * Function for performing a query which depends on item's type
     * @param realm Realm instance
     * @return Set of query result
     */
    abstract fun performQuery(realm: Realm): RealmQuery<T>

    /**
     * Function which returns a name of sort field
     * @return Sort field's name
     */
    protected open fun getSortField(): String = SORT_FIELD

    /**
     * Function for ID generation for a new object
     * @return ID for a new object which will be saved to the local database
     */
    protected open fun generateId(): Long
            = performQuery(realm)
            .max(ID_FIELD)
            ?.toLong()
            ?.plus(1)
            ?: 1
}