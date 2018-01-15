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
     */
    fun bind(noteViewModel: NoteViewModel,
             noteClickListener: NotesAdapter.OnNoteClickListener) = with(itemView) {
        checkBox.visibility = if (noteViewModel.checked) View.VISIBLE else View.GONE
        val noteItem = noteViewModel.noteItem
        setOnClickListener { noteClickListener.onNoteClick(noteItem) }
        tvDescription.text = noteItem.description
        tvDate.text = noteItem.parseDate()
    }
}