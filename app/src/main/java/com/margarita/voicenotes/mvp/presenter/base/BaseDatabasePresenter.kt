package com.margarita.voicenotes.mvp.presenter.base

import com.margarita.voicenotes.models.entities.Category
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmQuery
import io.realm.Sort

/**
 * Base class for all presenters which contain methods for working with the local database
 * @param T A type of items which will be shown by presenter
 */
abstract class BaseDatabasePresenter<T: RealmObject> {

    /**
     * Realm instance for working with the local database
     */
    protected var realm: Realm = Realm.getDefaultInstance()

    companion object {
        const val ID_FIELD = "id"
        private const val SORT_FIELD = "date"

        /**
         * Field name of query for getting category by note ID
         */
        private const val NOTES_ID_FIELD = "notes.id"

        /**
         * Function for getting a note's category
         * @param noteId Note's ID
         */
        fun getCategory(noteId: Long): Category? {
            val realm = Realm.getDefaultInstance()
            val realmResults = realm
                    .where(Category::class.java)
                    .equalTo(NOTES_ID_FIELD, noteId)
                    .findFirst()
            return if (realmResults != null) realm.copyFromRealm(realmResults) else null
        }
    }

    /**
     * Function for loading all items of the concrete type from the local database
     */
    protected fun getData(): List<T> {
        val realmResults = performQuery(realm)
                .sort(getSortField(), getSortOrder())
                .findAll()
        return realm.copyFromRealm(realmResults)
    }

    /**
     * Function for removing an item with the given ID
     * @param id ID of item which will be removed
     */
    private fun remove(id: Long): Unit
            = realm.executeTransaction { realm1 ->
                performQuery(realm1)
                .equalTo(ID_FIELD, id)
                .findAll()
                .deleteAllFromRealm()
    }

    /**
     * Function for removing all items by the given list of IDs
     * @param ids IDs of items which will be removed
     */
    protected fun removeByIds(ids: Set<Long>): Unit = ids.forEach { remove(it) }

    /**
     * Function for removing all items in the table
     */
    protected fun removeAll(): Unit
            = realm.executeTransaction { realm1 ->
                performQuery(realm1)
                .findAll()
                .deleteAllFromRealm()
    }


    /**
     * Function for saving an object to the local database
     * @param realm Realm instance
     * @param realmObject Object which will be saved to the local database
     */
    protected fun save(realm: Realm, realmObject: RealmObject) {
        realm.copyToRealmOrUpdate(realmObject)
    }

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
     * Function which returns a sort order
     */
    protected open fun getSortOrder(): Sort = Sort.DESCENDING

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