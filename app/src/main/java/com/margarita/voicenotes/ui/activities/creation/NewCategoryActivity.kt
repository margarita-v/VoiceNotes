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
        setContentView(R.layout.activity_container)
        // Try to restore fragment
        val fragment = restoreFragment() as NewCategoryFragment? ?: NewCategoryFragment()
        setFragment(fragment)
    }
}
