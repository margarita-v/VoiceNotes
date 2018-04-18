package com.margarita.voicenotes.ui.fragments.dialogs.confirm

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.support.annotation.StringRes
import com.margarita.voicenotes.common.throwClassCastException

/**
 * Confirmation dialog fragment
 */
open class ConfirmDialogFragment: BaseConfrimDialogFragment(), DialogInterface.OnClickListener {

    /**
     * Callback to calling activity or fragment
     */
    private lateinit var confirmationListener: ConfirmationListener

    companion object {

        /**
         * Tag for a dialog showing
         */
        const val SHOWING_TAG = BaseConfrimDialogFragment.SHOWING_TAG

        /**
         * Tag for a dialog for confirmation of removing items
         */
        const val DELETE_CONFIRM_TAG = BaseConfrimDialogFragment.DELETE_CONFIRM_TAG

        /**
         * Function for creation an instance of confirmation dialog
         * @param messageRes String resource ID for the dialog's message
         * @param titleRes String resource ID for the dialog's title
         * @return Instance of confirmation dialog
         */
        fun newInstance(@StringRes messageRes: Int,
                        @StringRes titleRes: Int = BaseConfrimDialogFragment.DEFAULT_TITLE_RES,
                        tag: String = BaseConfrimDialogFragment.DEFAULT_USAGE_TAG)
                : ConfirmDialogFragment
                = configureDialog(ConfirmDialogFragment(), messageRes, titleRes, tag)
                    as ConfirmDialogFragment
    }

    /**
     * Tag for a dialog usage (optional field)
     */
    private var usageTag = DEFAULT_USAGE_TAG

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            confirmationListener = context as ConfirmationListener
        } catch (e: ClassCastException) {
            throwClassCastException(context, "ConfirmationListener")
        }
    }

    override fun onClick(dialogInterface: DialogInterface?, i: Int) {
        when (i) {
            Dialog.BUTTON_NEGATIVE -> dismiss()
            Dialog.BUTTON_POSITIVE -> {
                usageTag = arguments?.getString(TAG_KEY) ?: DEFAULT_USAGE_TAG
                confirmationListener.confirm(usageTag)
            }
        }
    }

    /**
     * Interface for callback implementation
     */
    interface ConfirmationListener {

        /**
         * Function which will be called if the user confirmed his action
         */
        fun confirm(tag: String)
    }
}