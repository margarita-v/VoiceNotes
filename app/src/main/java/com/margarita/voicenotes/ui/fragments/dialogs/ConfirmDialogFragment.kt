package com.margarita.voicenotes.ui.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.DialogFragment
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.throwClassCastException

/**
 * Confirmation dialog fragment
 */
class ConfirmDialogFragment: DialogFragment(), DialogInterface.OnClickListener {

    /**
     * Callback to calling activity or fragment
     */
    private lateinit var confirmationListener: ConfirmationListener

    companion object {

        /**
         * Tag for a dialog showing
         */
        const val SHOWING_TAG = "SHOWING_TAG"

        /**
         * Keys for Bundle
         */
        private const val TITLE_KEY = "TITLE_KEY"
        private const val MESSAGE_KEY = "MESSAGE_KEY"
        private const val TAG_KEY = "TAG_KEY"

        /**
         * Tag for a dialog for confirmation of removing items
         */
        const val DELETE_CONFIRM_TAG = "DELETE_CONFIRM_TAG"

        /**
         * Default tag for a dialog usage
         */
        private const val DEFAULT_USAGE_TAG = "DEFAULT_TAG"

        /**
         * String resources IDs for default title and message
         */
        private const val DEFAULT_TITLE_RES = R.string.confirm_title
        private const val DEFAULT_MESSAGE_RES = R.string.confirm_delete_notes

        /**
         * Function for creation an instance of confirmation dialog
         * @param messageRes String resource ID for the dialog's message
         * @param titleRes String resource ID for the dialog's title
         * @return Instance of confirmation dialog
         */
        fun newInstance(@StringRes messageRes: Int,
                        @StringRes titleRes: Int = DEFAULT_TITLE_RES,
                        tag: String = DEFAULT_USAGE_TAG): ConfirmDialogFragment {
            val dialog = ConfirmDialogFragment()
            val args = Bundle()
            args.putInt(TITLE_KEY, titleRes)
            args.putInt(MESSAGE_KEY, messageRes)
            args.putString(TAG_KEY, tag)
            dialog.arguments = args
            return dialog
        }
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
            = AlertDialog.Builder(context)
                .setTitle(arguments?.getInt(TITLE_KEY) ?: DEFAULT_TITLE_RES)
                .setMessage(arguments?.getInt(MESSAGE_KEY) ?: DEFAULT_MESSAGE_RES)
                .setPositiveButton(R.string.yes, this)
                .setNegativeButton(R.string.no, this)
                .create()

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