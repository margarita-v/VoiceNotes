package com.margarita.voicenotes.common.adapters.list

import android.view.View
import android.view.ViewGroup
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.*
import com.margarita.voicenotes.models.BaseViewModel
import com.margarita.voicenotes.models.NoteViewModel
import com.margarita.voicenotes.models.entities.NoteItem
import kotlinx.android.synthetic.main.item_note.view.*

/**
 * Adapter for a note items list
 */
class NotesAdapter(noteClickListener: OnItemClickListener<NoteItem>)
    : BaseListAdapter<NoteItem>(noteClickListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder
            = NoteViewHolder(parent.inflate(R.layout.item_note))

    override fun addAll(items: List<NoteItem>): Unit
            = items.forEach { this.items.add(NoteViewModel(it)) }

    override fun getItemId(viewModel: BaseViewModel<NoteItem>): Long = viewModel.item.id

    /**
     * View holder for a note item
     */
    inner class NoteViewHolder(view: View) : BaseViewHolder(view) {

        override fun bind(itemViewModel: BaseViewModel<NoteItem>): Unit = with(itemView) {
            val noteItem = itemViewModel.item
            tvDescription.setNoteText(noteItem.description, true)
            tvCategory.setCategoryName(getCategoryName(noteItem))
            tvDate.text = noteItem.parseDate()
            ivPhoto.loadImage(context, noteItem.getCroppedPhotoUri())
        }
    }
}