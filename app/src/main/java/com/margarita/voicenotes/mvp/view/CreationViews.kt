package com.margarita.voicenotes.mvp.view

import android.support.annotation.StringRes
import com.margarita.voicenotes.models.entities.Category

/**
 * Interface for all views which is used for an item's creation
 */
interface BaseNewItemView {

    /**
     * Show loading animation
     */
    fun showLoading()

    /**
     * Hide loading animation
     */
    fun hideLoading()

    /**
     * Show error message
     * @param messageRes String resource ID for a message text
     */
    fun showError(@StringRes messageRes: Int)

    /**
     * Function which will be called if the item was created and saved successfully
     */
    fun onCreationSuccess()

    /**
     * Function which will be called if the item was edited successfully
     */
    fun onEditSuccess()
}

/**
 * Interface for a view which implements creation of a new notes
 */
interface NewNoteView: BaseNewItemView {

    /**
     * Function for setting a list of categories
     * @param categories List of categories which will be set to the view
     */
    fun setCategories(categories: List<Category>)
}
