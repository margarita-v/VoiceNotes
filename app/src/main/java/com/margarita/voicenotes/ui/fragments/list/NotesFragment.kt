package com.margarita.voicenotes.ui.fragments.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.github.clans.fab.FloatingActionMenu
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.adapters.list.NotesAdapter
import com.margarita.voicenotes.models.entities.NoteItem
import com.margarita.voicenotes.mvp.presenter.list.NotesPresenter
import com.margarita.voicenotes.ui.activities.ViewNoteActivity
import com.margarita.voicenotes.ui.activities.creation.note.EditNoteActivity
import kotlinx.android.synthetic.main.fragment_list_notes.*

/**
 * Fragment for showing a list of notes
 */
class NotesFragment: BaseListFragment<NoteItem>() {

    /**
     * Intent for the note click events
     */
    private val clickNoteIntent by lazy { Intent(context!!, ViewNoteActivity::class.java) }

    /**
     * Intent for the note editing
     */
    private val editNoteIntent by lazy { Intent(context!!, EditNoteActivity::class.java) }

    init {
        adapter = NotesAdapter(itemClickListener)
        presenter = NotesPresenter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabNewNote.setOnClickListener(activityCallback)
        fabNewPhoto.setOnClickListener(activityCallback)
        fabNewCategory.setOnClickListener(activityCallback)
    }

    override fun getLayoutRes(): Int = R.layout.fragment_list_notes

    override fun getFabMenu(): FloatingActionMenu = fabMenu

    override fun getActionModeTitleRes(): Int = R.string.selected_notes

    override fun getConfirmDialogTitleRes(): Int = R.string.confirm_delete_notes

    override fun getDeletedItemsMessageRes(): Int = R.string.notes_deleted

    override fun getEmptyMessageRes(): Int = R.string.empty_note_list

    override fun getEmptyPictureRes(): Int = R.drawable.ic_note_gray_24dp

    override fun onItemClick(item: NoteItem): Unit
            = startActivity(clickNoteIntent.putExtra(getString(R.string.note_intent), item))

    override fun edit(item: NoteItem?) {
        editNoteIntent.putExtra(getString(R.string.note_intent), item)
        startActivityForResult(editNoteIntent, EDIT_REQUEST_CODE)
    }
}