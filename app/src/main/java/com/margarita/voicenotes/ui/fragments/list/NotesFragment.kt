package com.margarita.voicenotes.ui.fragments.list

import android.content.Intent
import android.support.annotation.StringRes
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.adapters.list.NotesAdapter
import com.margarita.voicenotes.common.hide
import com.margarita.voicenotes.common.show
import com.margarita.voicenotes.common.showToast
import com.margarita.voicenotes.models.entities.NoteItem
import com.margarita.voicenotes.mvp.presenter.NotesPresenter
import com.margarita.voicenotes.mvp.view.NotesView
import com.margarita.voicenotes.ui.activities.ViewNoteActivity
import kotlinx.android.synthetic.main.empty_view.*

/**
 * Fragment for showing a list of notes
 */
class NotesFragment: BaseListFragment<NoteItem>(), NotesView {

    init {
        adapter = NotesAdapter(itemClickListener)
        presenter = NotesPresenter(this)
    }

    override fun showItemInfo(item: NoteItem): Unit
            = startActivity(Intent(context, ViewNoteActivity::class.java)
                .putExtra(getString(R.string.note_intent), item))

    //region NotesView implementation
    override fun showLoading(): Unit = setupWidgets(true)

    override fun hideLoading(): Unit = setupWidgets(false)

    override fun showEmptyView(): Unit = layoutEmpty.show()

    override fun showError(@StringRes messageRes: Int): Unit = context!!.showToast(messageRes)

    override fun setItems(items: List<NoteItem>) {
        layoutEmpty.hide()
        adapter.setItems(items)
    }

    override fun onDataSetChanged(): Unit = presenter.loadItems()
    //endregion
}