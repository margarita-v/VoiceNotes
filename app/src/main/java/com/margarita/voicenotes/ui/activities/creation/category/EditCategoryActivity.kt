package com.margarita.voicenotes.ui.activities.creation.category

import android.os.Bundle

/**
 * Activity for edit of categories
 */
class EditCategoryActivity : NewCategoryActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO Show category's info
    }

    override fun usedForCreation(): Boolean = false
}
