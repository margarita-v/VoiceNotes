package com.margarita.voicenotes.common.adapters.list

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckBox
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.hide
import com.margarita.voicenotes.common.show
import com.margarita.voicenotes.models.view.BaseViewModel
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
     * List of IDs of checked notes
     */
    var checkedIds = HashSet<Long>()
        protected set

    /**
     * Flag which shows if the multi choice mode is on
     */
    var isMultiChoiceMode = false
        private set

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int): Unit
            = holder.bind(items[position], position, itemClickListener)

    override fun getItemCount(): Int = items.size

    protected abstract fun addAll(items: List<ItemType>)

    protected abstract fun addAllIds()

    protected abstract fun selectItem(item: BaseViewModel<ItemType>)

    /**
     * Function for getting a count of checked items
     */
    fun getCheckedItemCount(): Int = checkedIds.size

    /**
     * Function for checking if all notes are selected
     */
    fun isAllNotesSelected(): Boolean = getCheckedItemCount() == itemCount

    /**
     * Function for clear a list of notes
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
     * Function for performing a selection of all notes
     */
    fun selectAll() {
        addAllIds()
        isMultiChoiceMode = true
        notifyDataSetChanged()
    }

    /**
     * Function for a note item selection
     * @param position Position of selected note item
     * @return True if the multi choice mode is on, False otherwise
     */
    fun selectItem(position: Int): Boolean {
        checkItem(position)
        notifyDataSetChanged()
        return isMultiChoiceMode
    }

    /**
     * Function for a note item checking
     * @param position Position of checked note item
     */
    protected fun checkItem(position: Int) {
        // Change item's checked state
        val itemViewModel = items[position]
        val newState = !itemViewModel.checked
        items[position].checked = newState
        selectItem(itemViewModel)
        isMultiChoiceMode = getCheckedItemCount() > 0
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

    abstract inner class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        abstract fun bind(itemViewModel: BaseViewModel<ItemType>)

        fun bind(itemViewModel: BaseViewModel<ItemType>,
                 position: Int,
                 itemClickListener: OnItemClickListener<ItemType>): Unit = with(itemView) {
            bind(itemViewModel)
            setOnClickListener { itemClickListener.onItemClick(itemViewModel.item, position) }
            setOnLongClickListener { itemClickListener.onItemLongClick(position) }

            val checkBox = findViewById<CheckBox>(R.id.checkBox)
            if (isMultiChoiceMode) {
                checkBox.show()
                checkBox.setOnCheckedChangeListener { _, _ ->
                    checkItem(position)
                    setCardViewColor(checkBox.isChecked)
                    if (!isMultiChoiceMode)
                        notifyDataSetChanged()
                }
            } else
                checkBox.hide()
        }

        /**
         * Function for setting a background color for a cardview
         * @param isChecked Flag which shows if the note item was checked
         */
        protected fun setCardViewColor(isChecked: Boolean) {
            val cardView = itemView.findViewById<CardView>(R.id.cardView)
            cardView.setBackgroundResource(
                    if (isChecked)
                        R.color.colorChosenNote
                    else
                        R.color.colorDefault)
        }
    }

    /**
     * Interface for an item click event handling
     */
    interface OnItemClickListener<in ItemType> {

        /**
         * Function for performing an item click event
         * @param item Item which was clicked
         * @param position Position of chosen note item
         */
        fun onItemClick(item: ItemType, position: Int)

        /**
         * Function for performing a long click event
         * @param position Position of chosen item
         */
        fun onItemLongClick(position: Int): Boolean
    }
}