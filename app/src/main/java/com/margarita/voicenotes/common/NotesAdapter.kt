package com.margarita.voicenotes.common

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.margarita.voicenotes.R
import com.margarita.voicenotes.models.NoteItem
import com.margarita.voicenotes.ui.holders.NoteViewHolder

/**
 * Adapter for a note items list
 */
class NotesAdapter(private val noteClickListener: OnNoteClickListener):
        RecyclerView.Adapter<NoteViewHolder>() {

    /**
     * List of notes
     */
    private var notes = ArrayList<NoteItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            NoteViewHolder(parent.inflate(R.layout.item_note))

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) =
            holder.bind(notes[position], noteClickListener)

    override fun getItemCount() = notes.size

    /**
     * Function for setting a list of notes to the adapter
     * @param notes List of note items which will be stored in the adapter
     */
    fun setNotes(notes: List<NoteItem>) {
        this.notes.clear()
        this.notes.addAll(notes)
        notifyDataSetChanged()
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
    }
}