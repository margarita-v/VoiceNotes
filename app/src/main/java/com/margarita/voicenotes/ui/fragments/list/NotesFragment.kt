package com.margarita.voicenotes.ui.fragments.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.adapters.list.NotesAdapter
import com.margarita.voicenotes.models.entities.NoteItem
import com.margarita.voicenotes.mvp.presenter.list.NotesPresenter
import com.margarita.voicenotes.ui.activities.creation.note.EditNoteActivity
import com.margarita.voicenotes.ui.activities.info.ViewNoteActivity
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

    private companion object {
        const val FAB_MENU_OPENED_FLAG = "FAB_MENU_OPENED"
    }

    init {
        adapter = NotesAdapter(itemClickListener)
        presenter = NotesPresenter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabMenu.setClosedOnTouchOutside(true)
        fabNewNote.setOnClickListener(activityCallback)
        fabNewPhoto.setOnClickListener(activityCallback)
        fabNewCategory.setOnClickListener(activityCallback)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (fabMenu.isOpened) {
            outState.putBoolean(FAB_MENU_OPENED_FLAG, true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val isFabMenuOpened = savedInstanceState?.getBoolean(FAB_MENU_OPENED_FLAG) ?: false
        if (isFabMenuOpened) {
            fabMenu.open(false)
        }
    }

    override fun getLayoutRes(): Int = R.layout.fragment_list_notes

    override fun getFabView(): View = fabMenu

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

    fun closeFabMenu(): Unit = fabMenu.close(false)
}