package com.margarita.voicenotes.ui.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.margarita.voicenotes.common.NotesAdapter
import com.margarita.voicenotes.models.NoteViewModel
import kotlinx.android.synthetic.main.item_note.view.*

/**
 * View holder for a note item
 */
class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

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

        val checked = noteViewModel.checked
        checkBox.visibility = if (checked) View.VISIBLE else View.GONE
        checkBox.isChecked = checked
        /*
        if (checked) {
            checkBox.setOnCheckedChangeListener { _, _ ->
                noteClickListener.onNoteLongClick(position)
            }
        } else {
            checkBox.setOnCheckedChangeListener(null)
        }*/
    }
}