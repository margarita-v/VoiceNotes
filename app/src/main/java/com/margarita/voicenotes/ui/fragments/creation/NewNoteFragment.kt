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
import kotlinx.android.synthetic.main.fragment_new_note.*
import java.util.*

/**
 * Fragment for creation a new note
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
            presenter.loadCategories()
        }

        // Configure all buttons
        configureEditWidgets(etNote, imgBtnClear)
        configureOptionalButtons(photoUri != null)
        imgBtnSpeak.setOnClickListener { selectedOptionCallback.speak() }
        imgBtnPhoto.setOnClickListener { selectedOptionCallback.takePhoto() }
        imgBtnChoosePhoto.setOnClickListener { selectedOptionCallback.pickImageFromGallery() }
        imgBtnCrop.setOnClickListener { selectedOptionCallback.cropImage(photoUri) }
        imgBtnDelete.setOnClickListener { deleteImage() }
        btnSave.setOnClickListener {
            val description = etNote.getTextAsString()
            val categoryId = adapter.getChosenItemId(spinnerCategory.selectedItemPosition)
            if (noteForEdit == null) {
                presenter.createNote(
                        description,
                        Calendar.getInstance().timeInMillis,
                        photoUri,
                        croppedPhotoUri,
                        categoryId)
            } else {
                presenter.editNote(
                        noteForEdit!!.id,
                        description,
                        photoUri,
                        croppedPhotoUri,
                        categoryId)
            }
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
     * Function for showing a note's info
     */
    private fun showNoteInfo(noteItem: NoteItem) {
        configureEditText(etNote, noteItem.description)

        val hasPhoto = noteItem.photoUri != null
        configureOptionalButtons(hasPhoto)

        photoUri = noteItem.photoUri?.parseStringToUri()
        croppedPhotoUri = noteItem.croppedPhotoUri?.parseStringToUri()

        if (hasPhoto) {
            ivPhoto.loadImage(context!!, croppedPhotoUri)
        }
        // Show a note's category name
        spinnerCategory.setSelection(
                adapter.getCategoryPosition(
                        BaseDatabasePresenter.getCategory(noteItem.id)))
    }

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