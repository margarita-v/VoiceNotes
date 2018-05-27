package com.margarita.voicenotes.ui.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.adapters.CategorySpinnerAdapter
import com.margarita.voicenotes.common.adapters.list.BaseListAdapter
import com.margarita.voicenotes.common.adapters.list.NotesAdapter
import com.margarita.voicenotes.common.hide
import com.margarita.voicenotes.common.show
import com.margarita.voicenotes.common.showToast
import com.margarita.voicenotes.models.entities.Category
import com.margarita.voicenotes.models.entities.NoteItem
import com.margarita.voicenotes.mvp.presenter.SearchNotesPresenter
import com.margarita.voicenotes.mvp.view.SearchView
import com.margarita.voicenotes.ui.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.fragment_new_note.*
import kotlinx.android.synthetic.main.list_content.*
import kotlinx.android.synthetic.main.progress_bar.*

class SearchNotesFragment : BaseFragment(), SearchView {

    /**
     * Presenter for searching
     */
    private val presenter by lazy { SearchNotesPresenter(this) }

    private val noteClickListener = object: BaseListAdapter.OnItemClickListener<NoteItem> {
        override fun onItemLongClick(position: Int): Boolean = false

        override fun onItemClick(item: NoteItem, position: Int) { }
    }

    /**
     * Adapter for spinner with categories
     */
    private val categoriesAdapter by lazy { CategorySpinnerAdapter(context!!) }

    /**
     * Adapter for searched notes
     */
    private val notesAdapter by lazy { NotesAdapter(noteClickListener) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivEmptyPhoto.setImageResource(R.drawable.ic_note_gray_24dp)
        tvEmpty.setText(R.string.empty_search)

        rvList.layoutManager = LinearLayoutManager(activity)
        rvList.adapter = notesAdapter

        spinnerCategory.adapter = categoriesAdapter
        if (categoriesAdapter.hasOnlyNoneCategory()) {
            presenter.loadCategories()
        }
    }

    fun search(query: String) = presenter.searchNotes(query)

    override fun getLayoutRes(): Int = R.layout.fragment_search

    override fun setCategories(items: List<Category>) = categoriesAdapter.addAll(items)

    override fun showLoading() = progressBar.show()

    override fun hideLoading() = progressBar.hide()

    override fun showEmptyView() = layoutEmpty.show()

    override fun showError(messageRes: Int) = context!!.showToast(messageRes)

    override fun setItems(items: List<NoteItem>) = notesAdapter.setItems(items)

    override fun onDataSetChanged() { }
}