package com.margarita.voicenotes.mvp.view

import android.support.annotation.StringRes
import com.margarita.voicenotes.models.entities.NoteItem

/**
 * Interface for a view which shows a list of notes
 */
interface NotesView {

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
     * Function for setting a new list of notes
     * @param notes List of notes which will be set to the view
     */
    fun setNotes(notes: List<NoteItem>)
}
