package com.margarita.voicenotes.ui.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.margarita.voicenotes.common.parseDate
import com.margarita.voicenotes.models.Note
import kotlinx.android.synthetic.main.item_note.view.*

/**
 * View holder for a note item
 */
class NoteViewHolder(view: View): RecyclerView.ViewHolder(view) {

    fun bind(note: Note) = with(itemView) {
        tvDescription.text = note.description
        tvDate.text = note.date.parseDate()
    }
}