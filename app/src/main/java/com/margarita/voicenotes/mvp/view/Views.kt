package com.margarita.voicenotes.mvp.view

import com.margarita.voicenotes.models.entities.Category
import com.margarita.voicenotes.models.entities.NoteItem

/**
 * Interface for a view which shows a list of notes
 */
interface NotesView: BaseView<NoteItem>

/**
 * Interface for a view which shows a list of categories
 */
interface CategoriesView: BaseView<Category>

/**
 * Interface for a view which implements a creation of a new notes
 */
interface NewNoteView: CategoriesView {

    /**
     * Function which will be called if the note was created and saved successfully
     */
    fun onCreationSuccess()

    //region Default implementations for unused methods
    override fun showEmptyView() { }

    override fun onDataSetChanged() { }
    //endregion
}
