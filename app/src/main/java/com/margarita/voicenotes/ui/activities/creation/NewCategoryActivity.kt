package com.margarita.voicenotes.ui.activities.creation

import android.os.Bundle
import com.margarita.voicenotes.R
import com.margarita.voicenotes.ui.fragments.creation.NewCategoryFragment

/**
 * Activity for a category creation, showing and editing
 */
class NewCategoryActivity : BaseNewItemActivity(R.string.create_category) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Try to restore fragment
        fragment = restoreFragment() as NewCategoryFragment? ?: NewCategoryFragment()
        setFragment(fragment)
    }
}
