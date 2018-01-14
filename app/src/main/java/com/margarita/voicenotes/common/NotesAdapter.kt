package com.margarita.voicenotes.common

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.margarita.voicenotes.R
import com.margarita.voicenotes.models.NoteItem
import com.margarita.voicenotes.ui.holders.NoteViewHolder

/**
 * Adapter for a note items list
 */
class NotesAdapter : RecyclerView.Adapter<NoteViewHolder>() {

    /**
     * List of notes
     */
    private var notes = ArrayList<NoteItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            NoteViewHolder(parent.inflate(R.layout.item_note))

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) =
            holder.bind(notes[position])

    override fun getItemCount() = notes.size

    /**
     * Function for getting a note by its position
     * @param position Position of note
     * @return Note at the given position
     */
    fun getItem(position: Int) = notes[position]

    /**
     * Function for setting a list of notes to the adapter
     * @param notes List of note items which will be stored in the adapter
     */
    fun setNotes(notes: List<NoteItem>) {
        this.notes.clear()
        this.notes.addAll(notes)
        notifyDataSetChanged()
    }
}