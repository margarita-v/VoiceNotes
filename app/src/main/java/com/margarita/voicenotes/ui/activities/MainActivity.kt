package com.margarita.voicenotes.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.replace
import com.margarita.voicenotes.ui.fragments.NotesFragment
import com.margarita.voicenotes.ui.fragments.dialogs.ConfirmDialogFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ConfirmDialogFragment.ConfirmationListener {

    /**
     * Fragment for showing a list of notes
     */
    private var notesFragment: NotesFragment? = null

    companion object {
        private const val FRAGMENT_TAG = "NOTE_FRAGMENT_TAG"
        private const val CONTAINER_ID = R.id.container
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        // Try to restore fragment
        notesFragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as NotesFragment?
        // Show restored fragment or create a new fragment and show it
        supportFragmentManager.replace(
                CONTAINER_ID,
                notesFragment ?: NotesFragment(),
                FRAGMENT_TAG)
    }

    override fun confirm(): Unit = notesFragment!!.removeChosenNotes()
}
