package com.margarita.voicenotes.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.adapters.MainFragmentPagerAdapter
import com.margarita.voicenotes.ui.fragments.BaseListFragment
import com.margarita.voicenotes.ui.fragments.NotesFragment
import com.margarita.voicenotes.ui.fragments.dialogs.ConfirmDialogFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :
        BaseActivity(),
        ConfirmDialogFragment.ConfirmationListener,
        BaseListFragment.OnFabClickListener {

    /**
     * Fragment for showing a list of notes
     */
    private lateinit var notesFragment: NotesFragment

    /**
     * Intent for creation a new note
     */
    private val createNoteIntent by lazy {
        Intent(this, NewNoteActivity::class.java)
    }

    companion object {
        private const val NEW_NOTE_REQUEST_CODE = 6
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewPager.adapter = MainFragmentPagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)

        // Try to restore fragment
        //notesFragment = restoreFragment() as NotesFragment? ?: NotesFragment()
        //setFragment(notesFragment)
    }

    override fun onFabClick(): Unit
            = startActivityForResult(createNoteIntent, NEW_NOTE_REQUEST_CODE)

    override fun confirm(): Unit = notesFragment.removeChosenNotes()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == NEW_NOTE_REQUEST_CODE) {
            notesFragment.onDataSetChanged()
        }
    }
}
