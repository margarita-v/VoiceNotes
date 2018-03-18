package com.margarita.voicenotes.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.margarita.voicenotes.R
import com.margarita.voicenotes.ui.activities.creation.category.NewCategoryActivity
import com.margarita.voicenotes.ui.activities.creation.note.NewNoteActivity
import com.margarita.voicenotes.ui.fragments.dialogs.ConfirmDialogFragment
import com.margarita.voicenotes.ui.fragments.list.BaseListFragment
import com.margarita.voicenotes.ui.fragments.list.NotesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :
        BaseActivity(),
        ConfirmDialogFragment.ConfirmationListener,
        BaseListFragment.MainActivityCallback {

    /**
     * Intent for creation a new note
     */
    private val createNoteIntent by lazy {
        Intent(this, NewNoteActivity::class.java)
    }

    /**
     * Intent for creation a new category
     */
    private val createCategoryIntent by lazy {
        Intent(this, NewCategoryActivity::class.java)
    }

    /**
     * Index of the current visible fragment
     */
    private var currentFragmentIndex: Int = 0

    /**
     * Flag which shows if we should reload a list of notes
     */
    private var needReloadNotes = false

    companion object {

        /**
         * Request codes
         */
        private const val NEW_NOTE_REQUEST_CODE = 6
        private const val NEW_CATEGORY_REQUEST_CODE = 7
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setFragment(restoreFragment() as NotesFragment? ?: NotesFragment())
    }

    override fun onFabClick() {
        /*
        when (getCurrentFragment()) {
            is NotesFragment ->
                startActivityForResult(createNoteIntent, NEW_NOTE_REQUEST_CODE)
            is CategoriesFragment ->
                startActivityForResult(createCategoryIntent, NEW_CATEGORY_REQUEST_CODE)
        }*/
    }

    override fun notesDataSetChanged() {
        needReloadNotes = true
    }

    override fun confirm() {
        //getCurrentFragment().removeChosenItems()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //getCurrentFragment().onDataSetChanged()
        }
    }

    /**
     * Function for finishing an ActionMode for the current fragment
     * @param index New index of selected fragment
     */
    private fun finishActionMode(index: Int) {
        // If the data set was changed and notes fragment is visible,
        // then we should reload a list of notes
        /*
        if (needReloadNotes && index == NOTE_FRAGMENT_INDEX) {
            //findFragmentByTag(index).onDataSetChanged()
            needReloadNotes = false
        }*/
        // Finish action mode, if the fragment was changed
        if (index != currentFragmentIndex) {
            //getCurrentFragment().finishActionMode()
            currentFragmentIndex = index
        }
    }
}
