package com.margarita.voicenotes.ui.fragments.creation

import android.content.Context
import android.os.Bundle
import android.view.View
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.getTextAsString
import com.margarita.voicenotes.common.setSpeechText
import com.margarita.voicenotes.common.throwClassCastException
import com.margarita.voicenotes.models.entities.Category
import com.margarita.voicenotes.mvp.presenter.creation.NewCategoryPresenter
import kotlinx.android.synthetic.main.fragment_new_category.*

/**
 * Fragment for creation or editing the categories
 */
class NewCategoryFragment: BaseNewItemFragment() {

    /**
     * Presenter for creation of categories
     */
    private val presenter by lazy { NewCategoryPresenter(this) }

    /**
     * Category for edit if the fragment is used for editing
     */
    var categoryForEdit: Category? = null

    /**
     * Listener for user's action callback
     */
    private lateinit var actionCallback: BaseSelectedOption

    override fun getLayoutRes(): Int = R.layout.fragment_new_category

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureEditWidgets(etCategory, imgBtnClear)

        imgBtnSpeak.setOnClickListener { actionCallback.speak() }
        btnSave.setOnClickListener { save() }
        if (categoryForEdit != null) {
            showCategoryInfo(categoryForEdit!!)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            actionCallback = context as BaseSelectedOption
        } catch (e: ClassCastException) {
            throwClassCastException(context, "BaseSelectedOption")
        }
    }

    override fun onCreationSuccess(): Unit
            = actionCallback.onCreationSuccess(R.string.category_created)

    override fun onEditSuccess(): Unit
            = actionCallback.onCreationSuccess(R.string.category_edited)

    override fun setText(text: String): Unit = etCategory.setSpeechText(text)

    override fun save() {
        val name = getCategoryName()
        if (categoryForEdit == null) {
            presenter.createCategory(name)
        } else {
            presenter.editCategory(categoryForEdit!!.id, name)
        }
    }

    /**
     * Function for gettting a name of category
     */
    fun getCategoryName(): String = etCategory.getTextAsString()

    /**
     * Function for showing a category's info
     */
    private fun showCategoryInfo(category: Category): Unit
            = configureEditText(etCategory, category.name)
}