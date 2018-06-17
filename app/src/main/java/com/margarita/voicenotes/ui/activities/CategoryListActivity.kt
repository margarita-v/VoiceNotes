package com.margarita.voicenotes.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.margarita.voicenotes.R
import com.margarita.voicenotes.ui.fragments.list.CategoriesFragment

/**
 * Activity for showing a list of categories
 */
class CategoryListActivity : BaseListActivity() {

    companion object {
        private const val CONTENT_CHANGED_KEY = "CONTENT_CHANGED_KEY"
    }

    private var isContentChanged: Boolean = false
    private lateinit var categoriesFragment: CategoriesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)
        isContentChanged = savedInstanceState?.getBoolean(CONTENT_CHANGED_KEY) ?: false
        categoriesFragment = restoreFragment() as CategoriesFragment? ?: CategoriesFragment()
        setFragment(categoriesFragment)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(CONTENT_CHANGED_KEY, isContentChanged)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && isContentChanged) {
            setResult(Activity.RESULT_OK)
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun confirm(tag: String) {
        isContentChanged = true
        categoriesFragment.removeChosenItems()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            categoriesFragment.onDataSetChanged()
        }
    }

    override fun onDataSetChanged() {
        isContentChanged = true
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.fabCategory -> createCategory()
        }
    }
}