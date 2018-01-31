package com.margarita.voicenotes.common.adapters

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.margarita.voicenotes.ui.fragments.list.BaseListFragment
import com.margarita.voicenotes.ui.fragments.list.CategoriesFragment
import com.margarita.voicenotes.ui.fragments.list.NotesFragment

/**
 * Adapter for a ViewPager
 */
class MainFragmentPagerAdapter(fragmentManager: FragmentManager)
    : FragmentPagerAdapter(fragmentManager) {

    companion object {

        /**
         * Max count of fragments
         */
        private const val FRAGMENTS_COUNT = 2

        /**
         * Set of titles for tabs
         */
        private val TITLES = arrayOf("Notes", "Categories")

        /**
         * Indexes of all fragments
         */
        const val NOTE_FRAGMENT_INDEX = 0
        const val CATEGORY_FRAGMENT_INDEX = 1
    }

    override fun getItem(position: Int): BaseListFragment<*> {
        return when (position) {
            NOTE_FRAGMENT_INDEX -> NotesFragment()
            else -> CategoriesFragment()
        }
    }

    override fun getCount(): Int = FRAGMENTS_COUNT

    override fun getPageTitle(position: Int): CharSequence? = TITLES[position]
}