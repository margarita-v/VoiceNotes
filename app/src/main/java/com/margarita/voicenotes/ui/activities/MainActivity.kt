package com.margarita.voicenotes.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.adapters.MainFragmentPagerAdapter
import com.margarita.voicenotes.ui.activities.creation.NewCategoryActivity
import com.margarita.voicenotes.ui.activities.creation.NewNoteActivity
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val currentFragmentIndex = savedInstanceState?.getInt(CURRENT_FRAGMENT_INDEX_KEY) ?: 0
        viewPager.adapter = MainFragmentPagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)

        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) { }

            override fun onPageScrolled(position: Int,
                                        positionOffset: Float,
                                        positionOffsetPixels: Int) { }

            override fun onPageSelected(position: Int) {
                if (position != currentFragmentIndex) {
                    getCurrentFragment().finishActionMode()
                }
            }
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

    override fun confirm(): Unit = getCurrentFragment().removeChosenItems()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            getCurrentFragment().onDataSetChanged()
        }
    }

    /**
     * Function for getting a current visible fragment from the view pager
     */
    private fun getCurrentFragment(): BaseListFragment<*>
            = supportFragmentManager
                .findFragmentByTag(TAG + viewPager.currentItem) as BaseListFragment<*>
}
