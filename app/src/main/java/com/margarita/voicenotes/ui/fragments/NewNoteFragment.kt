package com.margarita.voicenotes.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.*
import com.margarita.voicenotes.common.adapters.CategorySpinnerAdapter
import com.margarita.voicenotes.models.entities.Category
import com.margarita.voicenotes.mvp.presenter.NewNotePresenter
import com.margarita.voicenotes.mvp.view.NewNoteView
import kotlinx.android.synthetic.main.fragment_new_note.*
import kotlinx.android.synthetic.main.progress_bar.*
import java.util.*

/**
 * Fragment for creation a new note
 */
class NewNoteFragment: BaseFragment(), NewNoteView {

    /**
     * Presenter for creation of notes
     */
    private val presenter by lazy { NewNotePresenter(this) }

    /**
     * Adapter for a spinner with categories
     */
    private val adapter by lazy { CategorySpinnerAdapter(context!!) }

    /**
     * Listener for user's selected action callback
     */
    private lateinit var selectedOptionCallback: SelectedOption

    /**
     * Uri for the photo of note
     */
    var photoUri: Uri? = null

    /**
     * Uri for the cropped photo of note
     */
    private var croppedPhotoUri: Uri? = null

    override fun getLayoutRes(): Int = R.layout.fragment_new_note

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (croppedPhotoUri != null) {
            ivPhoto.loadImage(context!!, croppedPhotoUri!!)
        }

        // Configure spinner and load its items
        spinnerCategory.adapter = adapter
        if (adapter.hasOnlyNoneCategory()) {
            presenter.loadItems()
        }

        // Configure all buttons
        configureOptionalButtons(photoUri != null)
        imgBtnSpeak.setOnClickListener { selectedOptionCallback.startSpeechRecognition() }
        imgBtnPhoto.setOnClickListener { selectedOptionCallback.takePhoto() }
        imgBtnChoosePhoto.setOnClickListener { selectedOptionCallback.pickImageFromGallery() }
        imgBtnCrop.setOnClickListener { selectedOptionCallback.cropImage(photoUri) }
        imgBtnDelete.setOnClickListener { deleteImage() }
        btnSave.setOnClickListener {
            presenter.createNote(
                    etNote.text.toString(),
                    Calendar.getInstance().timeInMillis,
                    photoUri,
                    croppedPhotoUri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            selectedOptionCallback = context as SelectedOption
        } catch (e: ClassCastException) {
            throwClassCastException(context, "SelectedOption")
        }
    }

    override fun showLoading(): Unit = progressBar.show()

    override fun hideLoading(): Unit = progressBar.hide()

    override fun showError(messageRes: Int): Unit = context!!.showToast(messageRes)

    override fun setItems(items: List<Category>): Unit = adapter.addAll(items)

    override fun onCreationSuccess(): Unit = selectedOptionCallback.onCreationSuccess()

    /**
     * Function for delete a chosen photo of the note
     */
    private fun deleteImage() {
        ivPhoto.loadImage(context!!)
        photoUri = null
        croppedPhotoUri = null
        configureOptionalButtons(false)
    }

    /**
     * Function for configuration of states for optional buttons
     * @param enabled Flag which shows the state of optional buttons
     */
    private fun configureOptionalButtons(enabled: Boolean) {
        imgBtnCrop.setEnabledIconColor(enabled)
        imgBtnDelete.setEnabledIconColor(enabled)
    }

    /**
     * Function for setting a text of note
     * @param text A text of note
     */
    fun setText(text: String) {
        // Capitalize the first letter of note
        etNote.setText(text, TextView.BufferType.EDITABLE)
        etNote.setSelection(text.length)
    }

    /**
     * Function for cropping a note's photo
     * @param croppedPhotoUri Uri for a cropped photo
     */
    fun cropImage(croppedPhotoUri: Uri) {
        this.croppedPhotoUri = croppedPhotoUri
        ivPhoto.loadImage(context!!, croppedPhotoUri)
        configureOptionalButtons(true)
    }

    /**
     * Interface for performing callbacks to the activity
     * when the user choose some action
     */
    interface SelectedOption {

        /**
         * Function for launching speech recognition service
         */
        fun startSpeechRecognition()

        /**
         * Function for taking photo for note
         */
        fun takePhoto()

        /**
         * Function for choosing a photo for note from gallery
         */
        fun pickImageFromGallery()

        /**
         * Function for changing a photo's thumbnail
         */
        fun cropImage(photoUri: Uri?)

        /**
         * Function which will be called if the note was created and saved successfully
         */
        fun onCreationSuccess()
    }
}