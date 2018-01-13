package com.margarita.voicenotes.mvp.presenter

import com.margarita.voicenotes.mvp.view.OpenedNoteView

class OpenedNotePresenter(private val view: OpenedNoteView,
                          private val noteId: Int) {

    fun loadCategoryName() {
        //TODO Get category name from database
    }
}