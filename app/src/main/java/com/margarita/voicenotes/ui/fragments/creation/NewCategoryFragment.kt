package com.margarita.voicenotes.ui.fragments.creation

import android.content.Context
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.throwClassCastException

/**
 * Fragment for creation a new category
 */
class NewCategoryFragment: BaseNewItemFragment() {

    /**
     * Listener for user's action callback
     */
    private lateinit var actionCallback: BaseSelectedOption

    override fun getLayoutRes(): Int = R.layout.fragment_new_category

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            actionCallback = context as BaseSelectedOption
        } catch (e: ClassCastException) {
            throwClassCastException(context, "BaseSelectedOption")
        }
    }

    override fun onCreationSuccess(): Unit
            = actionCallback.onCreationSuccess(R.string.category_created)
}