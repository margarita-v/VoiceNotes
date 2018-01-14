package com.margarita.voicenotes.ui.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.margarita.voicenotes.models.NoteItem
import kotlinx.android.synthetic.main.item_note.view.*

/**
 * View holder for a note item
 */
class NoteViewHolder(view: View): RecyclerView.ViewHolder(view) {

    /**
     * Function for binding a noteItem item and showing its info in view
     * @param noteItem NoteItem item which info will be shown
     */
    fun bind(noteItem: NoteItem) = with(itemView) {
        tvDescription.text = noteItem.description
        tvDate.text = noteItem.parseDate()
    }
}