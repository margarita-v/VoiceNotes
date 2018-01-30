package com.margarita.voicenotes.ui.fragments.creation

import android.content.Context
import android.os.Bundle
import android.view.View
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.getTextAsString
import com.margarita.voicenotes.common.setSpeechText
import com.margarita.voicenotes.common.throwClassCastException
import com.margarita.voicenotes.mvp.presenter.creation.NewCategoryPresenter
import kotlinx.android.synthetic.main.fragment_new_category.*

/**
 * Fragment for creation a new category
 */
class NewCategoryFragment: BaseNewItemFragment() {

    /**
     * Presenter for creation of categories
     */
    private val presenter by lazy { NewCategoryPresenter(this) }

    /**
     * Listener for user's action callback
     */
    private lateinit var actionCallback: BaseSelectedOption

    override fun getLayoutRes(): Int = R.layout.fragment_new_category

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imgBtnSpeak.setOnClickListener { actionCallback.speak() }
        btnSave.setOnClickListener { presenter.createCategory(etCategory.getTextAsString()) }
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

    override fun setText(text: String): Unit = etCategory.setSpeechText(text)
}