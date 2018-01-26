package com.margarita.voicenotes.common

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
         * Layout for a spinner's dropdown item
         */
        private const val SPINNER_DROPDOWN_LAYOUT =
                android.R.layout.simple_spinner_dropdown_item
    }

    fun hasOnlyNoneCategory() : Boolean = count == 1
}