package com.margarita.voicenotes.ui.activities.creation.category

import android.os.Bundle
import com.margarita.voicenotes.R
import com.margarita.voicenotes.models.entities.Category

/**
 * Activity for edit of categories
 */
class EditCategoryActivity : NewCategoryActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Show category's info
        val category = getParcelableExtra(R.string.category_intent) as Category
        newCategoryFragment.showCategoryInfo(category)
    }

    override fun usedForCreation(): Boolean = false
}
