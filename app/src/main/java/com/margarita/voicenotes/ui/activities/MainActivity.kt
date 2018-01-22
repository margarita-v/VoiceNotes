package com.margarita.voicenotes.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.support.annotation.StringRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.*
import com.margarita.voicenotes.models.NoteItem
import com.margarita.voicenotes.mvp.view.NotesView
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.progress_bar.*

class MainActivity : AppCompatActivity(), NotesView {

    /**
     * Translation Y value for a floating action button for its animation
     */
    private val fabTranslationYForHide by lazy { fab.width * 1.5f }

    /**
     * Listener for a note click event
     */
    private val noteClickListener = object: NotesAdapter.OnNoteClickListener {
        override fun onNoteClick(noteItem: NoteItem, position: Int) {
            if (!adapter.isMultiChoiceMode) {
                showNoteInfo(noteItem)
            } else {
                selectItem(position)
            }
        }

        override fun onNoteLongClick(position: Int): Boolean {
            selectItem(position)
            return true
        }

        /**
         * Function for a note item selection
         * @param position Position of selected note item
         */
        private fun selectItem(position: Int): Unit
                // If multi choice mode is on, fab should not be visible
                = fab.setVisible(!adapter.selectItem(position))
    }

    /**
     * Adapter for RecyclerView
     */
    private val adapter : NotesAdapter by lazy { NotesAdapter(noteClickListener) }

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

    override fun showLoading(): Unit = setupWidgets(true)

    override fun hideLoading(): Unit = setupWidgets(false)

    override fun showEmptyView(): Unit = setupEmptyView(true)

    override fun showError(@StringRes messageRes: Int): Unit = showToast(messageRes)

    override fun setNotes(notes: List<NoteItem>) {
        setupEmptyView(false)
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

    /**
     * Setup visibility for widgets
     * @param isLoading Flag which shows if the loading is performing
     */
    private fun setupWidgets(isLoading: Boolean) {
        progressBar.setVisible(isLoading)
        fab.setVisible(!isLoading)
    }

    /**
     * Setup visibility for an empty view
     * @param isVisible Flag which shows if the empty view should be visible
     */
    private fun setupEmptyView(isVisible: Boolean): Unit = rootLayout.setVisible(isVisible)

    /**
     * Function for showing the note's info
     * @param noteItem Note which info will be shown
     */
    private fun showNoteInfo(noteItem: NoteItem): Unit
            = startActivity(Intent(this, ViewNoteActivity::class.java)
                .putExtra(getString(R.string.note_intent), noteItem))
}
