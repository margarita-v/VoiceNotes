package com.margarita.voicenotes.common.adapters.list

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckBox
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.hide
import com.margarita.voicenotes.common.show
import com.margarita.voicenotes.models.BaseViewModel
import io.realm.RealmObject

/**
 * Base adapter for all lists
 */
abstract class BaseListAdapter<ItemType: RealmObject>(
        private val itemClickListener: BaseListAdapter.OnItemClickListener<ItemType>)

    : RecyclerView.Adapter<BaseListAdapter<ItemType>.BaseViewHolder>() {

    /**
     * List of items
     */
    protected val items: MutableList<BaseViewModel<ItemType>> = ArrayList()

    /**
     * List of IDs of checked items
     */
    var checkedIds = HashSet<Long>()
        protected set

    /**
     * Flag which shows if the multi choice mode is on
     */
    var isMultiChoiceMode = false
        private set

    /**
     * Position of last checked item (need for item's editing)
     */
    private var lastCheckedItemPosition = -1

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int): Unit
            = holder.bind(items[position], position, itemClickListener)

    override fun getItemCount(): Int = items.size

    /**
     * Function for getting the last checked item for its editing
     */
    fun getCheckedItem(): ItemType?
            = if (lastCheckedItemPosition > -1) items[lastCheckedItemPosition].item
            else null

    /**
     * Function for adding a list of items of a concrete type
     * @param items List of items which will be added
     */
    protected abstract fun addAll(items: List<ItemType>)

    /**
     * Function for getting an viewModel's ID which depends on viewModel type
     */
    protected abstract fun getItemId(viewModel: BaseViewModel<ItemType>): Long

    /**
     * Function for getting a count of checked items
     */
    fun getCheckedItemCount(): Int = checkedIds.size

    /**
     * Function for checking if all items are selected
     */
    fun isAllItemsSelected(): Boolean = getCheckedItemCount() == itemCount

    /**
     * Function for clear a list of items
     */
    fun clear() {
        items.clear()
        exitFromMultiChoice()
    }

    /**
     * Function for setting a list of items to the adapter
     * @param items List of items which will be stored in the adapter
     */
    fun setItems(items: List<ItemType>) {
        this.items.clear()
        addAll(items)
        exitFromMultiChoice()
    }

    /**
     * Function for performing a selection of all items
     */
    fun selectAll() {
        checkAllIds()
        isMultiChoiceMode = true
        notifyDataSetChanged()
    }

    /**
     * Function for an item selection
     * @param position Position of selected item
     * @return True if the multi choice mode is on, False otherwise
     */
    fun selectItem(position: Int): Boolean {
        checkItem(position)
        notifyDataSetChanged()
        return isMultiChoiceMode
    }

    /**
     * Function for an item checking
     * @param position Position of checked item
     */
    protected fun checkItem(position: Int) {
        // Change item's checked state
        val itemViewModel = items[position]
        val newState = !itemViewModel.checked
        itemViewModel.checked = newState

        val id = getItemId(itemViewModel)
        if (itemViewModel.checked) {
            checkedIds.add(id)
        } else {
            checkedIds.remove(id)
        }

        if (newState) {
            lastCheckedItemPosition = position
        }
        isMultiChoiceMode = getCheckedItemCount() > 0
    }

    /**
     * Function for adding all IDs to the list of checked IDs
     */
    private fun checkAllIds() {
        items.forEach {
            it.checked = true
            checkedIds.add(getItemId(it))
        }
    }

    /**
     * Function for clear item's selection
     */
    fun clearSelection() {
        items.forEach { it.checked = false }
        exitFromMultiChoice()
    }

    /**
     * Function for exit from the multi choice mode
     */
    private fun exitFromMultiChoice() {
        isMultiChoiceMode = false
        checkedIds.clear()
        notifyDataSetChanged()
    }

    /**
     * Base view holder for all view holders
     */
    abstract inner class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        /**
         * Function for binding a special fields for a concrete item
         */
        abstract fun bind(itemViewModel: BaseViewModel<ItemType>)

        /**
         * Common function for binding an item and showing its info in view
         * @param itemViewModel Item which info will be shown
         * @param position Position of chosen item
         * @param itemClickListener Listener of item click events
         */
        fun bind(itemViewModel: BaseViewModel<ItemType>,
                 position: Int,
                 itemClickListener: OnItemClickListener<ItemType>): Unit = with(itemView) {
            bind(itemViewModel)
            setOnClickListener { itemClickListener.onItemClick(itemViewModel.item, position) }
            setOnLongClickListener { itemClickListener.onItemLongClick(position) }

            val checkBox = findViewById<CheckBox>(R.id.checkBox)
            checkBox.setOnCheckedChangeListener(null)
            val checked = itemViewModel.checked
            checkBox.isChecked = checked
            setItemBackground(checked)

            if (isMultiChoiceMode) {
                checkBox.show()
                checkBox.setOnCheckedChangeListener { _, _ ->
                    checkItem(position)
                    setItemBackground(checkBox.isChecked)
                    if (!isMultiChoiceMode) {
                        notifyDataSetChanged()
                    }
                }
            } else {
                checkBox.hide()
            }
        }

        /**
         * Function for setting a background color for item
         * @param isChecked Flag which shows if the item was checked
         */
        protected open fun setItemBackground(isChecked: Boolean) {
            val cardView = itemView.findViewById<CardView>(R.id.cardView)
            cardView.setBackgroundResource(
                    if (isChecked) R.color.colorChosenItem else R.color.colorWhite)
        }
    }

    /**
     * Interface for an item click event handling
     */
    interface OnItemClickListener<in ItemType> {

        /**
         * Function for performing an item click event
         * @param item Item which was clicked
         * @param position Position of chosen item
         */
        fun onItemClick(item: ItemType, position: Int)

        /**
         * Function for performing a long click event
         * @param position Position of chosen item
         */
        fun onItemLongClick(position: Int): Boolean
    }
}