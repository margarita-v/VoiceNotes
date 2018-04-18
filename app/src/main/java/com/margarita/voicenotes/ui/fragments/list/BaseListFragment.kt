package com.margarita.voicenotes.ui.fragments.list

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.*
import com.margarita.voicenotes.common.adapters.list.BaseListAdapter
import com.margarita.voicenotes.mvp.presenter.list.BaseListPresenter
import com.margarita.voicenotes.mvp.view.BaseView
import com.margarita.voicenotes.ui.fragments.base.BaseFragment
import com.margarita.voicenotes.ui.fragments.dialogs.confirm.ConfirmDialogFragment
import io.realm.RealmObject
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.list_content.*
import kotlinx.android.synthetic.main.progress_bar.*

/**
 * Base fragment for showing a list of items
 */
abstract class BaseListFragment<ItemType: RealmObject>
    : BaseFragment(), BaseView<ItemType> {

    companion object {

        /**
         * Action mode for a contextual toolbar
         */
        private var actionMode: ActionMode? = null

        /**
         * Request code for item editing
         */
        const val EDIT_REQUEST_CODE = 5

        private const val FAB_INVISIBLE_FLAG = "FAB_INVISIBLE"
    }

    /**
     * Translation Y value for a floating action button view for its animation
     */
    private val fabTranslationYForHide by lazy { getFabView().width * 1.5f }

    /**
     * Listener for contextual toolbar
     */
    private val actionModeCallback: ActionModeCallback by lazy { ActionModeCallback() }

    /**
     * Listener for performing callbacks to the activity
     */
    protected lateinit var activityCallback: ListActivityCallback

    /**
     * Listener for an item click event
     */
    protected val itemClickListener = object:
            BaseListAdapter.OnItemClickListener<ItemType> {
        override fun onItemClick(item: ItemType, position: Int) {
            if (!adapter.isMultiChoiceMode) {
                onItemClick(item)
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
         * Function for an item selection
         * @param position Position of selected item
         */
        private fun selectItem(position: Int) {
            // If multi choice mode is on, fab should not be visible
            getFabView().setVisible(!adapter.selectItem(position))
            setupActionModeTitle()
            if (!adapter.isMultiChoiceMode) {
                finishActionMode()
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
    protected lateinit var presenter: BaseListPresenter<ItemType>

    /**
     * Function for showing the contextual toolbar
     */
    private fun startActionMode() {
        actionMode = activity?.startActionMode(actionModeCallback)
    }

    /**
     * Function for finishing an ActionMode
     */
    fun finishActionMode(): Unit? = actionMode?.finish()

    /**
     * Function for setting a title to the contextual toolbar
     */
    private fun setupActionModeTitle(): Unit?
            = actionMode?.setSelectedItemsCount(
                context!!, getActionModeTitleRes(), adapter.getCheckedItemCount())

    //region Abstract functions for getting resources
    /**
     * Function for getting a string resource ID for setting a title to the ActionMode
     */
    @StringRes protected abstract fun getActionModeTitleRes(): Int

    /**
     * Function for getting a string resource ID for setting a title
     * to the dialog for delete confirmation
     */
    @StringRes protected abstract fun getConfirmDialogTitleRes(): Int

    /**
     * Function for getting a string resource ID for showing a message for deleted items
     */
    @StringRes protected abstract fun getDeletedItemsMessageRes(): Int

    /**
     * Function for getting a string resource ID for setting a message of empty view
     */
    @StringRes protected abstract fun getEmptyMessageRes(): Int

    /**
     * Function for getting a drawable resource ID for setting an icon of empty view
     */
    @DrawableRes protected abstract fun getEmptyPictureRes(): Int
    //endregion

    protected abstract fun getFabView(): View

    /**
     * Function which will be called when the list item was clicked
     * and multi choice mode is off
     */
    protected abstract fun onItemClick(item: ItemType)

    /**
     * Function for edit of chosen item
     */
    protected abstract fun edit(item: ItemType?)

    /**
     * Function for checking if activity should reload data from the local database
     */
    open fun needReload(): Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivEmptyPhoto.setImageResource(getEmptyPictureRes())
        tvEmpty.setText(getEmptyMessageRes())

        // Setup adapter and contextual toolbar
        // if it was shown before configuration change
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
        })
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            activityCallback = context as ListActivityCallback
        } catch (e: ClassCastException) {
            throwClassCastException(context, "ListActivityCallback")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (getFabView().visibility != View.VISIBLE) {
            outState.putBoolean(FAB_INVISIBLE_FLAG, true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val isInvisible = savedInstanceState?.getBoolean(FAB_INVISIBLE_FLAG) ?: false
        if (isInvisible) {
            getFabView().hide()
        }
    }

    //region BaseView implementation
    override fun showLoading(): Unit = setupWidgets(true)

    override fun hideLoading(): Unit = setupWidgets(false)

    override fun showEmptyView(): Unit = layoutEmpty.show()

    override fun showError(messageRes: Int): Unit = context!!.showToast(messageRes)

    override fun setItems(items: List<ItemType>) {
        layoutEmpty.hide()
        adapter.setItems(items)
    }

    override fun onDataSetChanged(): Unit = presenter.loadItems()
    //endregion

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            onDataSetChanged()
            notifyReloadNotes()
        }
        finishActionMode()
    }

    /**
     * Function for removing the chosen items
     */
    fun removeChosenItems() {
        if (adapter.isAllItemsSelected()) {
            adapter.clear()
            presenter.clear()
        } else {
            presenter.removeAll(adapter.checkedIds)
        }
        notifyReloadNotes()
        finishActionMode()
        context?.showToast(getDeletedItemsMessageRes())
    }

    /**
     * Function which notifies the activity about the data set changes
     */
    private fun notifyReloadNotes() {
        if (needReload()) {
            activityCallback.onDataSetChanged()
        }
    }

    /**
     * Setup visibility for widgets
     * @param isLoading Flag which shows if the loading is performing
     */
    private fun setupWidgets(isLoading: Boolean) {
        progressBar.setVisible(isLoading)
        getFabView().setVisible(!isLoading)
    }

    /**
     * Function for showing animation for hide and show a floating action button
     * @param isForHide If True then we should hide FAB, show otherwise
     */
    private fun fabAnimate(isForHide: Boolean) {
        val translationY = if (isForHide) fabTranslationYForHide else 0f
        getFabView().animate().translationY(translationY).start()
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
                            .newInstance(getConfirmDialogTitleRes())
                            .show(fragmentManager, ConfirmDialogFragment.SHOWING_TAG)

                R.id.action_edit -> edit(adapter.getCheckedItem())

                R.id.action_select_all ->
                    if (adapter.isAllItemsSelected()) {
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
            getFabView().show()
            fabAnimate(false)
            adapter.clearSelection()
            actionMode = null
        }
    }

    /**
     * Interface for performing callbacks to the activity
     */
    interface ListActivityCallback: View.OnClickListener {

        /**
         * Function which notify activity if the data set was changed
         */
        fun onDataSetChanged()
    }
}