package com.margarita.voicenotes.common

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.margarita.voicenotes.R
import com.margarita.voicenotes.models.NoteItem
import com.margarita.voicenotes.models.NoteViewModel
import kotlinx.android.synthetic.main.item_note.view.*

/**
 * Adapter for a note items list
 */
class NotesAdapter(private val noteClickListener: OnNoteClickListener)
    : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    /**
     * List of notes
     */
    private var notes: MutableList<NoteViewModel> = ArrayList()

    /**
     * Flag which shows if the multi choice mode is on
     */
    var isMultiChoiceMode = false
        private set

    /**
     * Count of checked items
     */
    var checkedItemsCount = 0
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder
            = NoteViewHolder(parent.inflate(R.layout.item_note))

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int): Unit
            = holder.bind(notes[position], position, noteClickListener)

    override fun getItemCount(): Int = notes.size

    /**
     * Function for setting a list of notes to the adapter
     * @param notes List of note items which will be stored in the adapter
     */
    fun setNotes(notes: List<NoteItem>) {
        this.notes.clear()
        notes.forEach { this.notes.add(NoteViewModel(it)) }
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
    private fun checkItem(position: Int) {
        // Change item's checked state
        val prevChecked = notes[position].checked
        notes[position].checked = !prevChecked

        // Multi choice mode was started
        if (checkedItemsCount == 0) {
            isMultiChoiceMode = true
            checkedItemsCount++
        } else {
            // Check if the item was deselected
            val offset = if (prevChecked) -1 else 1
            checkedItemsCount += offset

            // Check if the last item was deselected
            isMultiChoiceMode = checkedItemsCount > 0
        }
    }

    /**
     * Function for clear item's selection
     */
    fun clearSelection() {
        notes.forEach { it.checked = false }
        exitFromMultiChoice()
    }

    /**
     * Function for removing the checked items
     */
    fun removeCheckedItems() {
        notes = notes.filter { !it.checked }.toMutableList()
        exitFromMultiChoice()
    }

    /**
     * Function for exit from the multi choice mode
     */
    private fun exitFromMultiChoice() {
        isMultiChoiceMode = false
        checkedItemsCount = 0
        notifyDataSetChanged()
    }

    /**
     * View holder for a note item
     */
    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        /**
         * Function for binding a noteItem item and showing its info in view
         * @param noteViewModel Note which info will be shown
         * @param position Position of chosen note
         * @param noteClickListener Listener of note click events
         */
        fun bind(noteViewModel: NoteViewModel,
                 position: Int,
                 noteClickListener: NotesAdapter.OnNoteClickListener): Unit = with(itemView) {
            val noteItem = noteViewModel.noteItem
            setOnClickListener { noteClickListener.onNoteClick(noteItem, position) }
            setOnLongClickListener { noteClickListener.onNoteLongClick(position) }
            tvDescription.text = noteItem.description
            tvDate.text = noteItem.parseDate()
            checkBox.setOnCheckedChangeListener(null)

            val checked = noteViewModel.checked
            checkBox.isChecked = checked
            setCardViewColor(checked)

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
        private fun setCardViewColor(isChecked: Boolean): Unit
                = itemView.cardView.setBackgroundResource(
                if (isChecked)
                    R.color.colorChosenNote
                else
                    R.color.colorDefault)
    }

    /**
     * Interface for a note click event handling
     */
    interface OnNoteClickListener {

        /**
         * Function for performing a note click event
         * @param noteItem Note item which was clicked
         * @param position Position of chosen note item
         */
        fun onNoteClick(noteItem: NoteItem, position: Int)

        /**
         * Function for performing a long click event
         * @param position Position of chosen note item
         */
        fun onNoteLongClick(position: Int): Boolean
    }
}