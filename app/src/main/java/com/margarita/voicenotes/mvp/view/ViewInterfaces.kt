package com.margarita.voicenotes.mvp.view

import com.margarita.voicenotes.models.NoteItem

/**
 * Base interface for all views
 */
interface BaseMvpView {

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
     * @param message Error message which will be shown
     */
    fun showError(message: String)
}

/**
 * Interface for a view which shows a list of notes
 */
interface NotesView: BaseMvpView {

    /**
     * Function for setting a new list of notes
     * @param notes List of notes which will be set to the view
     */
    fun setNotes(notes: List<NoteItem>)
}

/**
 * Interface for a view which shows a concrete note
 */
interface OpenedNoteView: BaseMvpView {

    /**
     * Function for showing a category name
     * @param name Name of category
     */
    fun showCategoryName(name: String)
}
