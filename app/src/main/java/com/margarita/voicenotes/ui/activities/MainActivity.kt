package com.margarita.voicenotes.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.adapters.MainFragmentPagerAdapter
import com.margarita.voicenotes.ui.activities.creation.NewCategoryActivity
import com.margarita.voicenotes.ui.activities.creation.NewNoteActivity
import com.margarita.voicenotes.ui.fragments.base.BaseFragment
import com.margarita.voicenotes.ui.fragments.dialogs.ConfirmDialogFragment
import com.margarita.voicenotes.ui.fragments.list.BaseListFragment
import com.margarita.voicenotes.ui.fragments.list.CategoriesFragment
import com.margarita.voicenotes.ui.fragments.list.NotesFragment
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

    /**
     * Intent for creation a new category
     */
    private val createCategoryIntent by lazy {
        Intent(this, NewCategoryActivity::class.java)
    }

    companion object {
        private const val NEW_NOTE_REQUEST_CODE = 6
        private const val NEW_CATEGORY_REQUEST_CODE = 7
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewPager.adapter = MainFragmentPagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onFabClick() {
        when (getCurrentFragment()) {
            is NotesFragment ->
                startActivityForResult(createNoteIntent, NEW_NOTE_REQUEST_CODE)
            is CategoriesFragment ->
                startActivityForResult(createCategoryIntent, NEW_CATEGORY_REQUEST_CODE)
        }
    }

    override fun confirm(): Unit = notesFragment.removeChosenItems()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == NEW_NOTE_REQUEST_CODE) {
            notesFragment.onDataSetChanged()
        }
    }

    /**
     * Function for getting a current visible fragment from the view pager
     */
    private fun getCurrentFragment(): BaseFragment?
            = supportFragmentManager.findFragmentByTag(
                "android:switcher:" +
                R.id.viewPager + ":" + viewPager.currentItem) as BaseFragment?
}
