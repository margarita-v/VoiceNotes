package com.margarita.voicenotes.ui.fragments.list

import android.content.Intent
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.adapters.list.NotesAdapter
import com.margarita.voicenotes.models.entities.NoteItem
import com.margarita.voicenotes.mvp.presenter.list.NotesPresenter
import com.margarita.voicenotes.ui.activities.ViewNoteActivity

/**
 * Fragment for showing a list of notes
 */
class NotesFragment: BaseListFragment<NoteItem>() {

    init {
        adapter = NotesAdapter(itemClickListener)
        presenter = NotesPresenter(this)
    }

    override fun showItemInfo(item: NoteItem): Unit
            = startActivity(Intent(context, ViewNoteActivity::class.java)
                .putExtra(getString(R.string.note_intent), item))

    override fun getEmptyMessageRes(): Int = R.string.empty_note_list

    override fun getEmptyPictureRes(): Int = R.drawable.ic_note_gray_24dp
}