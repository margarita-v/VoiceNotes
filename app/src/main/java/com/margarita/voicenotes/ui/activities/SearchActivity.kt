package com.margarita.voicenotes.ui.activities

import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import android.view.Menu
import com.margarita.voicenotes.R
import com.margarita.voicenotes.ui.fragments.SearchNotesFragment

class SearchActivity : BaseActivity() {

    private lateinit var searchFragment: SearchNotesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchFragment = restoreFragment() as SearchNotesFragment? ?: SearchNotesFragment()
        setFragment(searchFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView.requestFocus()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                searchFragment.search(query)
                searchView.clearFocus()
                return true
            }


            override fun onQueryTextChange(newText: String): Boolean = false
        })
        return true
    }
}
