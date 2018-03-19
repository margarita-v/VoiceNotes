package com.margarita.voicenotes.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.margarita.voicenotes.R
import com.margarita.voicenotes.ui.activities.creation.note.NewNoteActivity
import com.margarita.voicenotes.ui.fragments.list.NotesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseListActivity() {

    /**
     * Intent for creation a new note
     */
    private val createNoteIntent by lazy {
        Intent(this, NewNoteActivity::class.java)
    }

    /**
     * Intent for showing a list of categories
     */
    private val showCategoriesIntent by lazy {
        Intent(this, CategoryListActivity::class.java)
    }

    /**
     * Flag which shows if we should reload a list of notes
     */
    private var needReloadNotes = false

    private lateinit var notesFragment: NotesFragment

    companion object {

        /**
         * Request codes
         */
        private const val NEW_NOTE_REQUEST_CODE = 6
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        notesFragment = restoreFragment() as NotesFragment? ?: NotesFragment()
        setFragment(notesFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_categories, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_show_categories -> {
                //TODO Start activity for result and reload a list of notes
                //TODO if categories were changed!
                startActivity(showCategoriesIntent)
                closeFabMenu()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onClick(view: View) {
        when (view.id) {
            R.id.fabNewNote ->
                startActivityForResult(createNoteIntent, NEW_NOTE_REQUEST_CODE)
            R.id.fabNewCategory -> createCategory()
        }
        closeFabMenu()
    }

    override fun onDataSetChanged() {
        needReloadNotes = true
    }

    override fun confirm(): Unit = notesFragment.removeChosenItems()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            notesFragment.onDataSetChanged()
        }
    }

    private fun closeFabMenu(): Unit = notesFragment.closeFabMenu()

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
    }
}
