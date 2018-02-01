package com.margarita.voicenotes.ui.activities.creation.category

import android.os.Bundle
import com.margarita.voicenotes.R
import com.margarita.voicenotes.ui.activities.creation.BaseNewItemActivity
import com.margarita.voicenotes.ui.fragments.creation.NewCategoryFragment

/**
 * Activity for creation of categories
 */
open class NewCategoryActivity : BaseNewItemActivity(R.string.create_category) {

    protected lateinit var newCategoryFragment: NewCategoryFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Try to restore fragment
        newCategoryFragment =
                restoreFragment() as NewCategoryFragment? ?: NewCategoryFragment()
        fragment = newCategoryFragment
        setFragment(fragment)
    }

    override fun usedForCreation(): Boolean = true
}
