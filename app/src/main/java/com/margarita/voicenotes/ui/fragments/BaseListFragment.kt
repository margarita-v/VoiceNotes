package com.margarita.voicenotes.ui.fragments

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.adapters.list.BaseListAdapter
import com.margarita.voicenotes.common.setSelectedItemsCount
import com.margarita.voicenotes.common.setVisible
import com.margarita.voicenotes.common.showToast
import com.margarita.voicenotes.common.throwClassCastException
import com.margarita.voicenotes.mvp.presenter.BasePresenter
import com.margarita.voicenotes.ui.fragments.dialogs.ConfirmDialogFragment
import io.realm.RealmObject
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.progress_bar.*

/**
 * Base fragment for showing a list of items
 */
abstract class BaseListFragment<ItemType: RealmObject>: BaseFragment() {

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
     * Listener for an item click event
     */
    protected val itemClickListener = object: BaseListAdapter.OnItemClickListener<ItemType> {
        override fun onItemClick(item: ItemType, position: Int) {
            if (!adapter.isMultiChoiceMode) {
                showItemInfo(item)
            } else {
                selectItem(position)
            }
        }

        override fun onItemLongClick(position: Int): Boolean {
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
     * Adapter for RecyclerView
     */
    protected lateinit var adapter: BaseListAdapter<ItemType>

    /**
     * Presenter for showing a list of items
     */
    protected lateinit var presenter: BasePresenter<ItemType>

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

    override fun getLayoutRes(): Int = R.layout.fragment_list

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
            throwClassCastException(context, "OnFabClickListener")
        }
    }

    protected abstract fun showItemInfo(item: ItemType)

    /**
     * Function for removing the chosen notes
     */
    fun removeChosenNotes() {
        if (adapter.isAllNotesSelected()) {
            adapter.clear()
            presenter.clear()
        } else {
            presenter.removeAll(adapter.checkedIds)
        }
        actionMode?.finish()
        context?.showToast(R.string.notes_deleted)
    }

    /**
     * Setup visibility for widgets
     * @param isLoading Flag which shows if the loading is performing
     */
    protected fun setupWidgets(isLoading: Boolean) {
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