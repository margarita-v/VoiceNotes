package com.margarita.voicenotes.mvp.presenter

import com.margarita.voicenotes.common.getCategory
import com.margarita.voicenotes.models.entities.Category
import com.margarita.voicenotes.models.entities.NoteItem
import com.margarita.voicenotes.mvp.view.SearchView
import io.realm.Case
import io.realm.Realm
import io.realm.Sort

/**
 * Presenter for searching
 */
class SearchNotesPresenter(private val view: SearchView) {

    /**
     * Realm instance for working with the local database
     */
    private var realm: Realm = Realm.getDefaultInstance()

    companion object {
        private const val CATEGORY_SORT_FIELD = "name"
        private const val NOTE_ITEM_SORT_FIELD = "date"
        private const val NOTE_ITEM_SEARCH_FIELD = "description"

        private val CATEGORY_SORT_ORDER = Sort.ASCENDING
        private val NOTE_ITEM_SORT_ORDER = Sort.DESCENDING
    }

    fun loadCategories() {
        view.showLoading()
        val items = getCategories()
        view.hideLoading()

        if (items.isEmpty()) {
            view.showEmptyView()
        } else {
            view.setCategories(items)
        }
    }

    fun searchNotes(text: String, categoryId: Long? = null) {
        view.showLoading()
        val items = getNotes(text)

        if (categoryId == null) {
            view.hideLoading()
            if (items.isEmpty()) {
                view.showEmptyView()
            } else {
                view.setItems(items)
            }
        } else {
            val filteredItems = filterNotes(items, categoryId)
            view.hideLoading()
            if (filteredItems.isEmpty()) {
                view.showEmptyView()
            } else {
                view.setItems(filteredItems)
            }
        }
    }

    private fun getCategories() : List<Category> {
        val realmResults = realm.where(Category::class.java)
                .sort(CATEGORY_SORT_FIELD, CATEGORY_SORT_ORDER)
                .findAll()
        return realm.copyFromRealm(realmResults)
    }

    private fun getNotes(text: String) : List<NoteItem> {
        val realmResults = realm.where(NoteItem::class.java)
                .contains(NOTE_ITEM_SEARCH_FIELD, text, Case.INSENSITIVE)
                .sort(NOTE_ITEM_SORT_FIELD, NOTE_ITEM_SORT_ORDER)
                .findAll()
        return realm.copyFromRealm(realmResults)
    }

    private fun filterNotes(notes: List<NoteItem>, categoryId: Long) : List<NoteItem> {
        return notes.filter { getCategory(it)?.id == categoryId }
    }
}