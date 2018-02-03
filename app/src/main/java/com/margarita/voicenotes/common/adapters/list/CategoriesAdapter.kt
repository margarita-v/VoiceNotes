package com.margarita.voicenotes.common.adapters.list

import android.view.View
import android.view.ViewGroup
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.inflate
import com.margarita.voicenotes.models.BaseViewModel
import com.margarita.voicenotes.models.CategoryViewModel
import com.margarita.voicenotes.models.entities.Category
import kotlinx.android.synthetic.main.item_category.view.*

/**
 * Adapter for a category items list
 */
class CategoriesAdapter(categoryClickListener: BaseListAdapter.OnItemClickListener<Category>)
    : BaseListAdapter<Category>(categoryClickListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder
            = CategoryViewHolder(parent.inflate(R.layout.item_category))

    override fun addAll(items: List<Category>): Unit
            = items.forEach { this.items.add(CategoryViewModel(it)) }

    override fun getItemId(viewModel: BaseViewModel<Category>): Long = viewModel.item.id

    /**
     * View holder for a category item
     */
    inner class CategoryViewHolder(view: View) : BaseViewHolder(view) {

        override fun bind(itemViewModel: BaseViewModel<Category>): Unit = with(itemView) {
            tvCategory.text = itemViewModel.item.name
        }

        override fun setItemBackground(isChecked: Boolean) {
            super.setItemBackground(isChecked)
            itemView.ivPhoto.setImageResource(
                    if (isChecked)
                        R.drawable.ic_collections_bookmark_primary_24dp
                    else
                        R.drawable.ic_collections_bookmark_gray_24dp)
        }
    }
}