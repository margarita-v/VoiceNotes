package com.margarita.voicenotes.common.adapters

import android.content.Context
import android.widget.ArrayAdapter
import com.margarita.voicenotes.R
import com.margarita.voicenotes.models.entities.Category

/**
 * Adapter for a spinner which contains a list of categories
 */
class CategorySpinnerAdapter(context: Context)
    : ArrayAdapter<Category>(context, SPINNER_DROPDOWN_LAYOUT) {

    /**
     * List of categories which is shown in the spinner
     */
    private val categories: MutableList<Category> = ArrayList()

    init {
        // Add the "None" category
        add(Category(name = context.getString(R.string.none_category)))
    }

    companion object {

        /**
         * Position for the "none" category
         */
        private const val NONE_CATEGORY_POSITION = 0

        /**
         * Layout for a spinner's dropdown item
         */
        private const val SPINNER_DROPDOWN_LAYOUT =
                android.R.layout.simple_spinner_dropdown_item
    }

    override fun add(category: Category) {
        super.add(category)
        categories.add(category)
    }

    override fun addAll(vararg items: Category) {
        super.addAll(*items)
        categories += items
    }

    override fun addAll(collection: Collection<Category>) {
        super.addAll(collection)
        categories.addAll(collection)
    }

    /**
     * Function for checking if the adapter only "none" category
     */
    fun hasOnlyNoneCategory() : Boolean = count == 1

    /**
     * Function for getting ID of chosen category
     * @param position Position of chosen category
     * @return ID of chosen category or null if no category was chosen
     */
    fun getChosenItemId(position: Int): Long?
            = if (position != NONE_CATEGORY_POSITION) getItem(position).id else null

    /**
     * Function for getting position of the note's category.
     * If the category not found, return position of none category
     */
    fun getCategoryPosition(category: Category?): Int {

        // If the category is null, return position of none category
        if (category == null)
            return NONE_CATEGORY_POSITION

        // Try to find position of given category
        return (0 until categories.size).firstOrNull { categories[it].id == category.id }
                ?: NONE_CATEGORY_POSITION
    }
}