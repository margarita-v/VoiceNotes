package com.margarita.voicenotes.ui.activities

import android.os.Bundle
import android.view.View
import com.margarita.voicenotes.R
import com.margarita.voicenotes.ui.fragments.list.CategoriesFragment

/**
 * Activity for showing a list of categories
 */
class CategoryListActivity : BaseListActivity() {

    private lateinit var categoriesFragment: CategoriesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        categoriesFragment = restoreFragment() as CategoriesFragment? ?: CategoriesFragment()
        setFragment(categoriesFragment)
    }

    override fun confirm(): Unit = categoriesFragment.removeChosenItems()

    override fun onDataSetChanged() {
        //TODO Do something
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.fabCategory -> createCategory()
        }
        categoriesFragment.closeFabMenu()
    }
}