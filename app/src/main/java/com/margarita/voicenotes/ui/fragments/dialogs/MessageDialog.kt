package com.margarita.voicenotes.ui.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.DialogFragment

/**
 * Dialog for showing the message
 */
class MessageDialog: DialogFragment(), DialogInterface.OnClickListener {

    companion object {

        /**
         * Tag for a dialog showing
         */
        const val MESSAGE_DIALOG_TAG = "MESSAGE_DIALOG"

        /**
         * Dialog's title
         */
        private const val TITLE = "Error"

        /**
         * Key for Bundle
         */
        private const val MESSAGE_KEY = "MESSAGE_KEY"

        /**
         * Function for creation an instance of message dialog
         * @param messageRes String resource ID for the dialog's message
         * @return Instance of message dialog
         */
        fun newInstance(@StringRes messageRes: Int): MessageDialog {
            val dialog = MessageDialog()
            val args = Bundle()
            args.putInt(MESSAGE_KEY, messageRes)
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
            = AlertDialog.Builder(context)
                .setTitle(TITLE)
                .setMessage(arguments!!.getInt(MESSAGE_KEY))
                .setPositiveButton(getString(android.R.string.ok), this)
                .create()

    override fun onClick(p0: DialogInterface?, p1: Int): Unit = dismiss()
}