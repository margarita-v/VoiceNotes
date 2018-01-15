package com.margarita.voicenotes.common

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.margarita.voicenotes.R
import com.margarita.voicenotes.models.NoteItem
import com.margarita.voicenotes.models.NoteViewModel
import com.margarita.voicenotes.ui.holders.NoteViewHolder

/**
 * Adapter for a note items list
 */
class NotesAdapter(private val noteClickListener: OnNoteClickListener):
        RecyclerView.Adapter<NoteViewHolder>() {

    /**
     * List of notes
     */
    private var notes = ArrayList<NoteViewModel>()

    /**
     * Flag which shows if the multi choice mode is on
     */
    private var isMultiChoiceMode = false

    /**
     * Count of checked items
     */
    private var checkedItemsCount = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            NoteViewHolder(parent.inflate(R.layout.item_note))

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) =
            holder.bind(notes[position], position, noteClickListener)

    override fun getItemCount() = notes.size

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
     */
    fun selectItem(position: Int) {
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
        notifyItemChanged(position)
    }

    /**
     * Interface for a note click event handling
     */
    interface OnNoteClickListener {

        /**
         * Function for performing a note click event
         * @param noteItem Note item which was clicked
         */
        fun onNoteClick(noteItem: NoteItem)

        /**
         * Function for performing a long click event
         * @param position Position of chosen note item
         */
        fun onNoteLongClick(position: Int): Boolean
    }
}