package com.margarita.voicenotes.mvp.view

import android.support.annotation.StringRes
import com.margarita.voicenotes.models.entities.Category
import com.margarita.voicenotes.models.entities.NoteItem

/**
 * Interface for searching
 */
interface SearchView {

    fun showLoading()

    fun hideLoading()

    fun showEmptyView()

    fun hideEmptyView()

    fun showError(@StringRes messageRes: Int)

    fun setCategories(items: List<Category>)

    fun setSearchResult(items: List<NoteItem>)
}