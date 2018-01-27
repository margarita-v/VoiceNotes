package com.margarita.voicenotes.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.*
import com.margarita.voicenotes.models.entities.NoteItem
import com.margarita.voicenotes.mvp.presenter.NotesPresenter
import com.margarita.voicenotes.mvp.view.NotesView
import com.margarita.voicenotes.ui.activities.ViewNoteActivity
import com.margarita.voicenotes.ui.fragments.dialogs.ConfirmDialogFragment
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.fragment_notes.*
import kotlinx.android.synthetic.main.progress_bar.*

/**
 * Fragment for showing a list of notes
 */
class NotesFragment: BaseFragment(), NotesView {

    companion object {
        /**
         * Message for the class cast exception
         */
        private const val CLASS_CAST_MESSAGE = " must implement OnFabClickListener interface"
    }

    /**
     * Translation Y value for a floating action button for its animation
     */
    private val fabTranslationYForHide by lazy { fab.width * 1.5f }

    /**
     * Listener for contextual toolbar
     */
    private val actionModeCallback: ActionModeCallback by lazy { ActionModeCallback() }

    /**
     * Listener for the FAB click event
     */
    private lateinit var fabClickListener: OnFabClickListener

    /**
     * Action mode for a contextual toolbar
     */
    private var actionMode: ActionMode? = null

    /**
     * Adapter for RecyclerView
     */
    private val adapter : NotesAdapter by lazy { NotesAdapter(noteClickListener) }

    /**
     * Presenter for showing a list of notes
     */
    private val presenter: NotesPresenter by lazy { NotesPresenter(this) }

    /**
     * Listener for a note click event
     */
    private val noteClickListener = object: NotesAdapter.OnNoteClickListener {
        override fun onNoteClick(noteItem: NoteItem, position: Int) {
            if (!adapter.isMultiChoiceMode) {
                showNoteInfo(noteItem)
            } else {
                selectItem(position)
            }
        }

        override fun onNoteLongClick(position: Int): Boolean {
            if (actionMode == null) {
                startActionMode()
            }
            selectItem(position)
            return true
        }

        /**
         * Function for a note item selection
         * @param position Position of selected note item
         */
        private fun selectItem(position: Int) {
            // If multi choice mode is on, fab should not be visible
            fab.setVisible(!adapter.selectItem(position))
            setupActionModeTitle()
            if (!adapter.isMultiChoiceMode) {
                actionMode?.finish()
            } else {
                actionMode?.invalidate()
            }
        }
    }

    /**
     * Function for showing the contextual toolbar
     */
    private fun startActionMode() {
        actionMode = activity?.startActionMode(actionModeCallback)
    }

    /**
     * Function for setting a title to the contextual toolbar
     */
    private fun setupActionModeTitle(): Unit?
            = actionMode?.setSelectedItemsCount(context!!, adapter.getCheckedItemCount())

    override fun getLayoutRes(): Int = R.layout.fragment_notes

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup adapter and contextual toolbar if it was shown before configuration change
        if (adapter.itemCount == 0) {
            presenter.loadItems()
        } else if (adapter.isMultiChoiceMode) {
            startActionMode()
            setupActionModeTitle()
        }

        // Setup RecyclerView
        rvList.layoutManager = LinearLayoutManager(activity)
        rvList.adapter = adapter
        rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                fabAnimate(dy > 0)
                super.onScrolled(recyclerView, dx, dy)
            }

            /**
             * Function for showing animation for hide and show a floating action button
             * @param isForHide If True then we should hide FAB, show otherwise
             */
            private fun fabAnimate(isForHide: Boolean) {
                val translationY = if (isForHide) fabTranslationYForHide else 0f
                fab.animate().translationY(translationY).start()
            }
        })
        fab.setOnClickListener { fabClickListener.onFabClick() }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            fabClickListener = context as OnFabClickListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + CLASS_CAST_MESSAGE)
        }
    }

    override fun showLoading(): Unit = setupWidgets(true)

    override fun hideLoading(): Unit = setupWidgets(false)

    override fun showEmptyView(): Unit = layoutEmpty.show()

    override fun showError(@StringRes messageRes: Int): Unit = context!!.showToast(messageRes)

    override fun setItems(items: List<NoteItem>) {
        layoutEmpty.hide()
        adapter.setNotes(items)
    }

    override fun onDataSetChanged() {
        adapter.clear()
        presenter.loadItems()
    }

    /**
     * Function for removing the chosen notes
     */
    fun removeChosenNotes() {
        if (adapter.isAllNotesSelected()) {
            presenter.clear()
        } else {
            presenter.removeAll(adapter.checkedIds)
        }
        //adapter.removeCheckedItems()
        actionMode?.finish()
        context?.showToast(R.string.notes_deleted)
    }

    /**
     * Function for showing the note's info
     * @param noteItem Note which info will be shown
     */
    private fun showNoteInfo(noteItem: NoteItem): Unit
            = startActivity(Intent(context, ViewNoteActivity::class.java)
            .putExtra(getString(R.string.note_intent), noteItem))

    /**
     * Setup visibility for widgets
     * @param isLoading Flag which shows if the loading is performing
     */
    private fun setupWidgets(isLoading: Boolean) {
        progressBar.setVisible(isLoading)
        fab.setVisible(!isLoading)
    }

    /**
     * Callbacks implementation for a contextual toolbar
     */
    inner class ActionModeCallback : ActionMode.Callback {

        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.menuInflater.inflate(R.menu.menu_context, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            val menuItemEdit = menu.findItem(R.id.action_edit)
            menuItemEdit.isVisible = adapter.getCheckedItemCount() == 1
            return true
        }

        override fun onActionItemClicked(mode: ActionMode, menuItem: MenuItem): Boolean {
            when (menuItem.itemId) {
                R.id.action_delete ->
                    ConfirmDialogFragment
                            .newInstance(R.string.confirm_delete)
                            .show(fragmentManager, ConfirmDialogFragment.CONFIRM_DIALOG_TAG)

                R.id.action_select_all ->
                        if (adapter.isAllNotesSelected()) {
                            adapter.clearSelection()
                            mode.finish()
                        } else {
                            adapter.selectAll()
                            mode.invalidate()
                            setupActionModeTitle()
                        }
            }
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            fab.show()
            adapter.clearSelection()
            actionMode = null
        }
    }

    /**
     * Interface for performing callback to activity when the FAB is clicked
     */
    interface OnFabClickListener {

        fun onFabClick()
    }
}