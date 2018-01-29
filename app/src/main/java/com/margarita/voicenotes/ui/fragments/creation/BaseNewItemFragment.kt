package com.margarita.voicenotes.ui.fragments.creation

import com.margarita.voicenotes.common.hide
import com.margarita.voicenotes.common.show
import com.margarita.voicenotes.common.showToast
import com.margarita.voicenotes.mvp.view.BaseNewItemView
import com.margarita.voicenotes.ui.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.progress_bar.*

/**
 * Base fragment for creation of all items
 */
abstract class BaseNewItemFragment: BaseFragment(), BaseNewItemView {

    override fun showLoading(): Unit = progressBar.show()

    override fun hideLoading(): Unit = progressBar.hide()

    override fun showError(messageRes: Int): Unit= context!!.showToast(messageRes)

    /**
     * Base interface for performing callbacks to the activity
     */
    interface BaseSelectedOption {

        /**
         * Function for launching speech recognition service
         */
        fun speak()

        /**
         * Function which will be called if the note was created and saved successfully
         */
        fun onCreationSuccess()
    }
}