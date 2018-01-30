package com.margarita.voicenotes.common.adapters.list

import android.view.View
import android.view.ViewGroup
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.inflate
import com.margarita.voicenotes.common.loadImage
import com.margarita.voicenotes.common.parseStringToUri
import com.margarita.voicenotes.common.setCategoryName
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

    override fun checkAllIds() {
        items.forEach {
            it.checked = true
            checkedIds.add(it.item.id)
        }
    }

    override fun selectItem(item: BaseViewModel<NoteItem>) {
        val id = item.item.id
        if (item.checked) {
            checkedIds.add(id)
        } else {
            checkedIds.remove(id)
        }
    }

    /**
     * View holder for a note item
     */
    inner class NoteViewHolder(view: View) : BaseViewHolder(view) {

        override fun bind(itemViewModel: BaseViewModel<NoteItem>): Unit = with(itemView) {
            val noteItem = itemViewModel.item
            tvDescription.text = noteItem.description
            tvCategory.setCategoryName(noteItem.getCategoryName())
            tvDate.text = noteItem.parseDate()
            ivPhoto.loadImage(context, noteItem.croppedPhotoUri?.parseStringToUri())
        }
    }
}