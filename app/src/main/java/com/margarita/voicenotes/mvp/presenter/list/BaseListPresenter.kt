package com.margarita.voicenotes.mvp.presenter.list

import com.margarita.voicenotes.mvp.presenter.base.BaseDatabasePresenter
import com.margarita.voicenotes.mvp.view.BaseView
import io.realm.RealmObject

/**
 * Base class for all presenters which show a list of items and use a view
 * @param T A type of items which will be shown by presenter
 */
abstract class BaseListPresenter<T: RealmObject>(private val view: BaseView<T>)
    : BaseDatabasePresenter<T>() {

    /**
     * Function for loading all items of the concrete type from the local database
     */
    fun loadItems() {
        view.showLoading()
        val items = getData()
        view.hideLoading()

        if (items.isEmpty()) {
            view.showEmptyView()
        } else {
            view.setItems(items)
        }
    }

    /**
     * Function for removing all items by the given list of IDs
     * @param ids IDs of items which will be removed
     */
    fun removeAll(ids: Set<Long>) {
        view.showLoading()
        removeByIds(ids)
        view.hideLoading()
        view.onDataSetChanged()
    }

    /**
     * Function for removing all items in the table
     */
    fun clear() {
        view.showLoading()
        removeAll()
        view.hideLoading()
        view.onDataSetChanged()
    }
}