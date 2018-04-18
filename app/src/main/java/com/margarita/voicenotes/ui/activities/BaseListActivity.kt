package com.margarita.voicenotes.ui.activities

import android.content.Intent
import com.margarita.voicenotes.ui.activities.creation.category.NewCategoryActivity
import com.margarita.voicenotes.ui.fragments.dialogs.confirm.ConfirmDialogFragment
import com.margarita.voicenotes.ui.fragments.list.BaseListFragment

/**
 * Base class for all activities which contain a list of items
 */
abstract class BaseListActivity :
        BaseActivity(),
        ConfirmDialogFragment.ConfirmationListener,
        BaseListFragment.ListActivityCallback {

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
        private const val NEW_CATEGORY_REQUEST_CODE = 7
    }

    protected fun createCategory(): Unit
            = startActivityForResult(createCategoryIntent, NEW_CATEGORY_REQUEST_CODE)
}