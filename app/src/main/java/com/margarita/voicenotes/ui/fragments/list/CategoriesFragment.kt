package com.margarita.voicenotes.ui.fragments.list

import android.app.Activity
import android.content.Intent
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.adapters.list.CategoriesAdapter
import com.margarita.voicenotes.models.entities.Category
import com.margarita.voicenotes.mvp.presenter.list.CategoriesPresenter
import com.margarita.voicenotes.ui.activities.creation.category.EditCategoryActivity

/**
 * Fragment for showing a list of categories
 */
class CategoriesFragment: BaseListFragment<Category>() {

    /**
     * Intent for category editing
     */
    private val editCategoryIntent by lazy {
        Intent(context!!, EditCategoryActivity::class.java)
    }

    init {
        adapter = CategoriesAdapter(itemClickListener)
        presenter = CategoriesPresenter(this)
    }

    override fun getActionModeTitleRes(): Int = R.string.selected_categories

    override fun getConfirmDialogTitleRes(): Int = R.string.confirm_delete_categories

    override fun getDeletedItemsMessageRes(): Int = R.string.categories_deleted

    override fun getEmptyMessageRes(): Int = R.string.empty_category_list

    override fun getEmptyPictureRes(): Int = R.drawable.ic_collections_bookmark_gray_24dp

    override fun showItemInfo(item: Category) {

    }

    override fun edit(item: Category?) {
        editCategoryIntent.putExtra(context?.getString(R.string.category_intent), item)
        startActivityForResult(editCategoryIntent, EDIT_REQUEST_CODE)
    }

    override fun needReload(): Boolean = true
}