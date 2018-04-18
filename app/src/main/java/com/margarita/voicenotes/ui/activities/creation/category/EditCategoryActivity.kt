package com.margarita.voicenotes.ui.activities.creation.category

import android.os.Bundle
import com.margarita.voicenotes.R
import com.margarita.voicenotes.models.entities.Category
import com.margarita.voicenotes.ui.fragments.dialogs.confirm.CancelEditDialogFragment

/**
 * Activity for edit of categories
 */
class EditCategoryActivity : NewCategoryActivity(), CancelEditDialogFragment.CancelEditListener {

    /**
     * Category which will be edited
     */
    private lateinit var categoryForEdit: Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryForEdit = getParcelableExtra(R.string.category_intent) as Category

        // Show category's info
        newCategoryFragment.categoryForEdit = categoryForEdit
    }

    override fun onBackPressed() {
        val newCategoryName = newCategoryFragment.getCategoryName()
        if (newCategoryName.isEmpty() || newCategoryName == categoryForEdit.name) {
            finish()
        } else {
            showCancelDialog()
        }
    }

    override fun save() = newCategoryFragment.save()

    override fun cancelSaving() = finish()

    override fun usedForCreation(): Boolean = false
}
