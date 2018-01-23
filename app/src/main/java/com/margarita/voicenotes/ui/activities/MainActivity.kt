package com.margarita.voicenotes.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.replace
import com.margarita.voicenotes.ui.fragments.NotesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val FRAGMENT_TAG = "NOTE_FRAGMENT_TAG"
        private const val CONTAINER_ID = R.id.container
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        // Try to restore fragment
        val fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as NotesFragment?
        // Show restored fragment or create a new fragment and show it
        supportFragmentManager.replace(
                CONTAINER_ID,
                fragment ?: NotesFragment(),
                FRAGMENT_TAG)
    }
}
