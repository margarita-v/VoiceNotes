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
        const val CONFIRM_DIALOG_TAG = "CONFIRM_DIALOG_TAG"

        /**
         * Keys for Bundle
         */
        private const val TITLE_KEY = "TITLE_KEY"
        private const val MESSAGE_KEY = "MESSAGE_KEY"

        /**
         * String resources IDs for default title and message
         */
        private const val DEFAULT_TITLE_RES = R.string.confirm_title
        private const val DEFAULT_MESSAGE_RES = R.string.confirm_delete

        /**
         * Function for creation an instance of confirmation dialog
         * @param messageRes String resource ID for the dialog's message
         * @param titleRes String resource ID for the dialog's title
         * @return Instance of confirmation dialog
         */
        fun newInstance(@StringRes messageRes: Int,
                        @StringRes titleRes: Int = DEFAULT_TITLE_RES): ConfirmDialogFragment {
            val dialog = ConfirmDialogFragment()
            val args = Bundle()
            args.putInt(TITLE_KEY, titleRes)
            args.putInt(MESSAGE_KEY, messageRes)
            dialog.arguments = args
            return dialog
        }
    }

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
            Dialog.BUTTON_POSITIVE -> confirmationListener.confirm()
        }
    }

    /**
     * Interface for callback implementation
     */
    interface ConfirmationListener {

        /**
         * Function which will be called if the user confirmed his action
         */
        fun confirm()
    }
}