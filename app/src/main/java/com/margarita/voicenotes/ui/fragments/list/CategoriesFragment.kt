package com.margarita.voicenotes.ui.fragments.list

import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.adapters.list.CategoriesAdapter
import com.margarita.voicenotes.models.entities.Category
import com.margarita.voicenotes.mvp.presenter.CategoriesPresenter

/**
 * Fragment for showing a list of categories
 */
class CategoriesFragment: BaseListFragment<Category>() {

    init {
        adapter = CategoriesAdapter(itemClickListener)
        presenter = CategoriesPresenter(this)
    }

    override fun getEmptyMessageRes(): Int = R.string.empty_category_list

    override fun getEmptyPictureRes(): Int = R.drawable.ic_collections_bookmark_gray_24dp

    override fun showItemInfo(item: Category) {

    }
}