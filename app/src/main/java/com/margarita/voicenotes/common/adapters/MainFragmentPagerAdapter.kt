package com.margarita.voicenotes.common.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
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
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> NotesFragment()
            else -> CategoriesFragment()
        }
    }

    override fun getCount(): Int = FRAGMENTS_COUNT

    override fun getPageTitle(position: Int): CharSequence? = TITLES[position]
}