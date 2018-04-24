package com.margarita.voicenotes.ui.fragments.creation

import android.support.annotation.StringRes
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import com.margarita.voicenotes.common.*
import com.margarita.voicenotes.mvp.view.BaseNewItemView
import com.margarita.voicenotes.ui.fragments.base.BaseFragment
import com.margarita.voicenotes.ui.fragments.dialogs.MessageDialog
import kotlinx.android.synthetic.main.progress_bar.*

/**
 * Base fragment for creation of all items
 */
abstract class BaseNewItemFragment: BaseFragment(), BaseNewItemView {

    override fun showLoading(): Unit = progressBar.show()

    override fun hideLoading(): Unit = progressBar.hide()

    override fun showError(messageRes: Int): Unit
            = MessageDialog.newInstance(messageRes)
                .show(fragmentManager, MessageDialog.MESSAGE_DIALOG_TAG)

    /**
     * Function for configuration of the edit text and its clearing button
     */
    protected fun configureEditWidgets(editText: EditText, imageButton: ImageButton) {

        // Configure image button for performing the EditText clearing
        imageButton.setEnabledIconColor(editText.getTextAsString().isNotEmpty())
        imageButton.setOnClickListener { editText.setText("") }

        // Configure EditText
        editText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                imageButton.setEnabledIconColor(p0.isNotEmpty())
            }
        })

    }

    /**
     * Function for configuration of the EditText
     */
    protected fun configureEditText(editText: EditText, text: String) {
        // Disable the Soft Keyboard from appearing by default
        activity!!.window
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        editText.setSpeechText(text)
        editText.isCursorVisible = true
    }

    /**
     * Function for setting a speech result to the EditText
     */
    abstract fun setText(text: String)

    /**
     * Function for saving an item to the database
     */
    abstract fun save()

    /**
     * Base interface for performing callbacks to the activity
     */
    interface BaseSelectedOption {

        /**
         * Function for launching speech recognition service
         */
        fun speak()

        /**
         * Function which will be called if the item was created and saved successfully
         * @param messageRes String resource ID for a toast message
         */
        fun onCreationSuccess(@StringRes messageRes: Int)
    }
}