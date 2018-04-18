package com.margarita.voicenotes.ui.fragments.dialogs.confirm

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.support.annotation.StringRes
import com.margarita.voicenotes.common.throwClassCastException

/**
 * Class for showing a confirmation dialog for cancel of editing note
 */
class CancelEditDialogFragment: BaseConfrimDialogFragment() {

    /**
     * Callback to calling activity or fragment
     */
    private lateinit var cancelEditListener: CancelEditListener

    companion object {

        /**
         * Tag for a dialog showing
         */
        const val SHOWING_TAG = BaseConfrimDialogFragment.SHOWING_TAG

        /**
         * Function for creation an instance of dialog
         */
        fun newInstance(@StringRes messageRes: Int): CancelEditDialogFragment
                = configureDialog(CancelEditDialogFragment(), messageRes)
                as CancelEditDialogFragment
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            cancelEditListener = context as CancelEditListener
        } catch (e: ClassCastException) {
            throwClassCastException(context, "CancelEditListener")
        }
    }

    override fun onClick(dialogInterface: DialogInterface?, i: Int) {
        when (i) {
            Dialog.BUTTON_NEGATIVE -> cancelEditListener.cancelSaving()
            Dialog.BUTTON_POSITIVE -> cancelEditListener.save()
        }
    }

    /**
     * Interface for callback implementation
     */
    interface CancelEditListener {

        /**
         * Function for saving changes
         */
        fun save()

        /**
         * Function for canceling of saving changes
         */
        fun cancelSaving()
    }
}