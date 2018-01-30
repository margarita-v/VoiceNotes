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
}