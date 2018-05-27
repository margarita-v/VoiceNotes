package com.margarita.voicenotes.mvp.view

import com.margarita.voicenotes.models.entities.Category
import com.margarita.voicenotes.models.entities.NoteItem

interface SearchView : BaseView<NoteItem> {

    fun setCategories(items: List<Category>)
}