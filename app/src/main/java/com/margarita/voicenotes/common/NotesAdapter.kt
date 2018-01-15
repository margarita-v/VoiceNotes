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
        checkItem(position)
        notifyItemChanged(position)
    }

    /**
     * Function for a note item checking
     * @param position Position of checked note item
     */
    fun checkItem(position: Int) {
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
                 noteClickListener: NotesAdapter.OnNoteClickListener) = with(itemView) {

            val noteItem = noteViewModel.noteItem
            setOnClickListener { noteClickListener.onNoteClick(noteItem) }
            setOnLongClickListener { noteClickListener.onNoteLongClick(position) }
            tvDescription.text = noteItem.description
            tvDate.text = noteItem.parseDate()
            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked = noteViewModel.checked
            setupCheckedUI()
            if (isMultiChoiceMode) {
                checkBox.setOnCheckedChangeListener { _, _ ->
                    checkItem(position)
                    setupCheckedUI()
                }
            }
        }

        /**
         * Function for UI setup for a current checked state
         */
        private fun setupCheckedUI() {
            with(itemView) {
                checkBox.visibility = if (checkBox.isChecked) View.VISIBLE else View.GONE
            }
        }
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