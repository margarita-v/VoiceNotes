package com.margarita.voicenotes.mvp.view

import android.support.annotation.StringRes
import io.realm.RealmObject

/**
 * Base interface for all views which show a lists of items
 */
interface BaseView<in ItemType: RealmObject> {

    /**
     * Show loading animation
     */
    fun showLoading()

    /**
     * Hide loading animation
     */
    fun hideLoading()

    /**
     * Show empty view if the list of notes is empty
     */
    fun showEmptyView()

    /**
     * Show error message
     * @param messageRes String resource ID for a message text
     */
    fun showError(@StringRes messageRes: Int)

    /**
     * Function for setting a new list of items
     * @param items List of items which will be set to the view
     */
    fun setItems(items: List<ItemType>)

    /**
     * Function which notifies a view if the data set was changed
     */
    fun onDataSetChanged()
}