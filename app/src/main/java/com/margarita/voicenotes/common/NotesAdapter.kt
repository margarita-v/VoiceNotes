package com.margarita.voicenotes.common

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.margarita.voicenotes.R
import com.margarita.voicenotes.models.NoteViewModel
import com.margarita.voicenotes.models.entities.NoteItem
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
     * List of IDs of checked notes
     */
    var checkedIds = HashSet<Long>()
        private set

    /**
     * Flag which shows if the multi choice mode is on
     */
    var isMultiChoiceMode = false
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder
            = NoteViewHolder(parent.inflate(R.layout.item_note))

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int): Unit
            = holder.bind(notes[position], position, noteClickListener)

    override fun getItemCount(): Int = notes.size

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
        notes.clear()
        exitFromMultiChoice()
    }

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
     * Function for performing a selection of all notes
     */
    fun selectAll() {
        notes.forEach {
            it.checked = true
            checkedIds.add(it.noteItem.id)
        }
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
    private fun checkItem(position: Int) {
        // Change item's checked state
        val noteViewModel = notes[position]
        val newState = !noteViewModel.checked
        notes[position].checked = newState

        val id = noteViewModel.noteItem.id
        if (newState) {
            checkedIds.add(id)
        } else {
            checkedIds.remove(id)
        }

        isMultiChoiceMode = getCheckedItemCount() > 0
    }

    /**
     * Function for clear item's selection
     */
    fun clearSelection() {
        notes.forEach { it.checked = false }
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

            ivPhoto.loadImage(context, noteItem.croppedPhotoUri?.parseStringToUri())

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