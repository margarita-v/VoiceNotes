package com.margarita.voicenotes.ui.fragments.creation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.*
import com.margarita.voicenotes.common.adapters.CategorySpinnerAdapter
import com.margarita.voicenotes.models.entities.Category
import com.margarita.voicenotes.models.entities.NoteItem
import com.margarita.voicenotes.mvp.presenter.base.BaseDatabasePresenter
import com.margarita.voicenotes.mvp.presenter.creation.NewNotePresenter
import com.margarita.voicenotes.mvp.view.NewNoteView
import com.margarita.voicenotes.ui.fragments.dialogs.confirm.ConfirmDialogFragment
import kotlinx.android.synthetic.main.fragment_new_note.*
import java.io.File
import java.util.*

/**
 * Fragment for creation or editing the notes
 */
class NewNoteFragment: BaseNewItemFragment(), NewNoteView {

    /**
     * Presenter for creation of notes
     */
    private val presenter by lazy { NewNotePresenter(this) }

    /**
     * Note for edit if the fragment is used for editing
     */
    var noteForEdit: NoteItem? = null

    /**
     * Adapter for a spinner with categories
     */
    private val adapter by lazy { CategorySpinnerAdapter(context!!) }

    /**
     * Listener for user's selected action callback
     */
    private lateinit var selectedOptionCallback: SelectedOption

    /**
     * Photo file for note
     */
    var photoFile: File? = null

    /**
     * Uri for the photo of note
     */
    var photoUri: Uri? = null

    /**
     * Uri for the cropped photo of note
     */
    var croppedPhotoUri: Uri? = null

    override fun getLayoutRes(): Int = R.layout.fragment_new_note

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (croppedPhotoUri != null) {
            setCroppedPhoto(croppedPhotoUri!!)
        }

        // Configure spinner and load its items
        spinnerCategory.adapter = adapter
        if (adapter.hasOnlyNoneCategory()) {
            presenter.loadCategories()
        }

        // Configure all buttons
        configureEditWidgets(etNote, imgBtnClear)
        configureOptionalButtons(photoUri != null)
        imgBtnSpeak.setOnClickListener { selectedOptionCallback.speak() }
        imgBtnPhoto.setOnClickListener { selectedOptionCallback.takePhoto() }
        imgBtnChoosePhoto.setOnClickListener { selectedOptionCallback.pickImageFromGallery() }
        imgBtnCrop.setOnClickListener { selectedOptionCallback.cropImage(photoUri) }
        imgBtnDelete.setOnClickListener {
            ConfirmDialogFragment.newInstance(
                    messageRes = R.string.confirm_delete_note_photo,
                    tag = ConfirmDialogFragment.DELETE_CONFIRM_TAG)
                    .show(fragmentManager, ConfirmDialogFragment.SHOWING_TAG)
        }
        btnSave.setOnClickListener { save() }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            selectedOptionCallback = context as SelectedOption
        } catch (e: ClassCastException) {
            throwClassCastException(context, "SelectedOption")
        }
    }

    override fun setCategories(categories: List<Category>) {
        adapter.addAll(categories)
        if (noteForEdit != null) {
            configureEditWidgets(etNote, imgBtnClear)
            showNoteInfo(noteForEdit!!)
        }
    }

    override fun onCreationSuccess(): Unit
            = selectedOptionCallback.onCreationSuccess(R.string.note_created)

    override fun onEditSuccess(): Unit
            = selectedOptionCallback.onCreationSuccess(R.string.note_edited)

    override fun setText(text: String): Unit = etNote.setSpeechText(text)

    /**
     * Function for saving a note
     */
    override fun save() {
        val description = getDescription()
        val categoryId = getCategoryId()
        if (noteForEdit == null) {
            presenter.createNote(
                    description = description,
                    date = Calendar.getInstance().timeInMillis,
                    photoUri = photoUri,
                    croppedPhotoUri = croppedPhotoUri,
                    categoryId = categoryId)
        } else {
            presenter.editNote(
                    id = noteForEdit!!.id,
                    description = description,
                    photoUri = photoUri,
                    croppedPhotoUri = croppedPhotoUri,
                    categoryId = categoryId)
        }
    }

    /**
     * Function for getting a note's description
     */
    fun getDescription(): String = etNote.getTextAsString()

    /**
     * Function for getting an ID of the note's category
     */
    fun getCategoryId(): Long? = adapter.getChosenItemId(spinnerCategory.selectedItemPosition)

    /**
     * Function for showing a note's info
     */
    private fun showNoteInfo(noteItem: NoteItem) {
        configureEditText(etNote, noteItem.description)
        configureOptionalButtons(photoUri != null)

        photoUri = noteItem.photoUri?.parseStringToUri()
        croppedPhotoUri = noteItem.croppedPhotoUri?.parseStringToUri()
        ivPhoto.loadImage(context!!, croppedPhotoUri)

        // Show a note's category name
        spinnerCategory.setSelection(
                adapter.getCategoryPosition(
                        BaseDatabasePresenter.getCategory(noteItem.id)))
    }

    /**
     * Function for delete a chosen photo of the note
     */
    fun deletePhoto() {
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
     * Function for setting a cropped photo of note
     * @param croppedPhotoUri Uri for a cropped photo
     */
    fun setCroppedPhoto(croppedPhotoUri: Uri) {
        this.croppedPhotoUri = croppedPhotoUri
        ivPhoto.loadImage(context!!, croppedPhotoUri)
        configureOptionalButtons(true)
    }

    /**
     * Interface for performing callbacks to the activity
     * when the user choose some action
     */
    interface SelectedOption: BaseSelectedOption {

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
    }
}