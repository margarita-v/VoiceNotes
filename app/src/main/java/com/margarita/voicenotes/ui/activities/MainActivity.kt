package com.margarita.voicenotes.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.adapters.MainFragmentPagerAdapter
import com.margarita.voicenotes.ui.activities.creation.category.NewCategoryActivity
import com.margarita.voicenotes.ui.activities.creation.note.NewNoteActivity
import com.margarita.voicenotes.ui.fragments.dialogs.ConfirmDialogFragment
import com.margarita.voicenotes.ui.fragments.list.BaseListFragment
import com.margarita.voicenotes.ui.fragments.list.CategoriesFragment
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

        /**
         * Tag for getting a current fragment from the view pager
         */
        private const val TAG = "android:switcher:" + R.id.viewPager + ":"

        /**
         * Bundle key for saving index of the current visible fragment
         */
        private const val CURRENT_FRAGMENT_INDEX_KEY = "CURRENT_FRAGMENT"

        /**
         * Index of the NotesFragment in the view pager
         */
        private const val NOTE_FRAGMENT_INDEX = MainFragmentPagerAdapter.NOTE_FRAGMENT_INDEX
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        currentFragmentIndex = savedInstanceState?.getInt(CURRENT_FRAGMENT_INDEX_KEY) ?: 0

        viewPager.adapter = MainFragmentPagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) { }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }

            override fun onTabSelected(tab: TabLayout.Tab): Unit
                    = finishActionMode(tab.position)
        })

        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) { }

            override fun onPageScrolled(position: Int,
                                        positionOffset: Float,
                                        positionOffsetPixels: Int) { }

            override fun onPageSelected(position: Int): Unit = finishActionMode(position)
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CURRENT_FRAGMENT_INDEX_KEY, viewPager.currentItem)
    }

    override fun onFabClick() {
        when (getCurrentFragment()) {
            is NotesFragment ->
                startActivityForResult(createNoteIntent, NEW_NOTE_REQUEST_CODE)
            is CategoriesFragment ->
                startActivityForResult(createCategoryIntent, NEW_CATEGORY_REQUEST_CODE)
        }
    }

    override fun notesDataSetChanged() {
        needReloadNotes = true
    }

    override fun confirm(): Unit = getCurrentFragment().removeChosenItems()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            getCurrentFragment().onDataSetChanged()
        }
    }

    /**
     * Function for finishing an ActionMode for the current fragment
     * @param index New index of selected fragment
     */
    private fun finishActionMode(index: Int) {
        // If the data set was changed and notes fragment is visible,
        // then we should reload a list of notes
        if (needReloadNotes && index == NOTE_FRAGMENT_INDEX) {
            findFragmentByTag(index).onDataSetChanged()
            needReloadNotes = false
        }
        // Finish action mode, if the fragment was changed
        if (index != currentFragmentIndex) {
            getCurrentFragment().finishActionMode()
            currentFragmentIndex = index
        }
    }

    /**
     * Function for getting a fragment by tag and its index in the view pager
     * @param fragmentIndex Index of fragment in the view pager
     */
    private fun findFragmentByTag(fragmentIndex: Int): BaseListFragment<*>
            = supportFragmentManager
                .findFragmentByTag(TAG + fragmentIndex) as BaseListFragment<*>

    /**
     * Function for getting a current visible fragment from the view pager
     */
    private fun getCurrentFragment(): BaseListFragment<*>
            = findFragmentByTag(viewPager.currentItem)
}
