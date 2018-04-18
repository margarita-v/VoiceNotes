package com.margarita.voicenotes.ui.fragments.dialogs.confirm

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.DialogFragment
import com.margarita.voicenotes.R

abstract class BaseConfrimDialogFragment: DialogFragment(), DialogInterface.OnClickListener {

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
        const val TAG_KEY = "TAG_KEY"

        /**
         * Tag for a dialog for confirmation of removing items
         */
        const val DELETE_CONFIRM_TAG = "DELETE_CONFIRM_TAG"

        /**
         * Default tag for a dialog usage
         */
        const val DEFAULT_USAGE_TAG = "DEFAULT_TAG"

        /**
         * String resources IDs for default title and message
         */
        const val DEFAULT_TITLE_RES = R.string.confirm_title
        const val DEFAULT_MESSAGE_RES = R.string.confirm_delete_notes

        /**
         * Function for a simple dialog configuration
         */
        fun configureDialog(dialog: DialogFragment,
                            @StringRes messageRes: Int,
                            @StringRes titleRes: Int = DEFAULT_TITLE_RES,
                            tag: String = DEFAULT_USAGE_TAG): DialogFragment {
            val args = Bundle()
            args.putInt(TITLE_KEY, titleRes)
            args.putInt(MESSAGE_KEY, messageRes)
            args.putString(TAG_KEY, tag)
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
            = AlertDialog.Builder(context)
            .setTitle(arguments?.getInt(TITLE_KEY) ?: DEFAULT_TITLE_RES)
            .setMessage(arguments?.getInt(MESSAGE_KEY) ?: DEFAULT_MESSAGE_RES)
            .setPositiveButton(R.string.yes, this)
            .setNegativeButton(R.string.no, this)
            .create()
}