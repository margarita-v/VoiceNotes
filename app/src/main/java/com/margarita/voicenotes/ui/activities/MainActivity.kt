package com.margarita.voicenotes.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.support.annotation.StringRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.NotesAdapter
import com.margarita.voicenotes.common.showToast
import com.margarita.voicenotes.models.NoteItem
import com.margarita.voicenotes.mvp.view.NotesView
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.progress_bar.*

class MainActivity : AppCompatActivity(), NotesView {

    /**
     * Translation Y value for a floating action button for its animation
     */
    private val fabTranslationYForHide by lazy {
        fab.width * 1.5f
    }

    /**
     * Adapter for RecyclerView
     */
    private val adapter by lazy {
        NotesAdapter()
    }

    /**
     * Lazy initialization for RecyclerView which will be executed once
     */
    private val notesList by lazy {
        rvList.setHasFixedSize(true)
        rvList.layoutManager = LinearLayoutManager(this)
        rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                fabAnimate(dy > 0)
                super.onScrolled(recyclerView, dx, dy)
            }

            /**
             * Function for showing animation for hide and show a floating action button
             * @param isForHide If True then we should hide FAB, show otherwise
             */
            private fun fabAnimate(isForHide: Boolean) {
                val translationY = if (isForHide) fabTranslationYForHide else 0f
                fab.animate().translationY(translationY).start()
            }
        })
        rvList
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupAdapter()
        fab.setOnClickListener {
            startActivity(Intent(this, NewNoteActivity::class.java))
        }
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun showError(@StringRes messageRes: Int) {
        showToast(messageRes)
    }

    override fun setNotes(notes: List<NoteItem>) {
        adapter.setNotes(notes)
    }

    /**
     * Function for initialization of the RecyclerView's adapter
     */
    private fun setupAdapter() {
        val notes = (1..10).map {
            NoteItem(
                    it,
                    "$it A very long text of note. What's up? How are you? I am writing a useful app for you! And what are you doing?",
                    1457207701L - it * 200
            )
        }
        adapter.setNotes(notes)
        notesList.adapter = adapter
    }
}
