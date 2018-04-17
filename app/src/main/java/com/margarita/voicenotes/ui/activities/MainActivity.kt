package com.margarita.voicenotes.ui.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.margarita.voicenotes.R
import com.margarita.voicenotes.ui.activities.creation.note.NewNoteActivity
import com.margarita.voicenotes.ui.activities.creation.note.NewPhotoNoteActivity
import com.margarita.voicenotes.ui.fragments.list.NotesFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : BaseListActivity() {

    /**
     * Intent for creation a new note
     */
    private val createNoteIntent by lazy {
        Intent(this, NewNoteActivity::class.java)
    }

    /**
     * Intent for showing a list of categories
     */
    private val showCategoriesIntent by lazy {
        Intent(this, CategoryListActivity::class.java)
    }

    /**
     * Flag which shows if we should reload a list of notes
     */
    private var needReloadNotes = false

    /**
     * Fragment for showing a list of notes
     */
    private lateinit var notesFragment: NotesFragment

    /**
     * Current photo for a new note
     */
    private var photoFile: File? = null

    /**
     * Uri for a new photo
     */
    private var photoUri: Uri? = null

    companion object {

        /**
         * Request codes
         */
        private const val NEW_NOTE_REQUEST_CODE = 6
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        notesFragment = restoreFragment() as NotesFragment? ?: NotesFragment()
        setFragment(notesFragment)

        photoFile = savedInstanceState?.getSerializable(PHOTO_FILE_KEY) as File?
        photoUri = savedInstanceState?.getParcelable(PHOTO_URI_KEY) as Uri?
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable(PHOTO_FILE_KEY, photoFile)
        outState?.putParcelable(PHOTO_URI_KEY, photoUri)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_categories, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_show_categories -> {
                //TODO Start activity for result and reload a list of notes
                //TODO if categories were changed!
                startActivity(showCategoriesIntent)
                closeFabMenu()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onClick(view: View) {
        when (view.id) {
            R.id.fabNewNote -> startActivityForResult(createNoteIntent, NEW_NOTE_REQUEST_CODE)
            R.id.fabNewPhoto -> {
                val takePhotoIntent = createPhotoIntent()
                if (checkIntentHandlers(takePhotoIntent)) {
                    photoFile = createPhotoFile()
                    photoUri = getPhotoUri(photoFile!!)
                    showPhotoActivity(takePhotoIntent, photoUri!!)
                }
            }
            R.id.fabNewCategory -> createCategory()
        }
        closeFabMenu()
    }

    override fun onDataSetChanged() {
        needReloadNotes = true
    }

    override fun confirm(tag: String): Unit = notesFragment.removeChosenItems()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_CANCELED -> {
                when (requestCode) {
                    NEW_NOTE_REQUEST_CODE,
                    TAKE_PHOTO_REQUEST_CODE,
                    CROP_PHOTO_REQUEST_CODE -> deletePhotoFile(photoFile)
                }
            }
            Activity.RESULT_OK -> {
                when (requestCode) {
                    // Result of creation of a new note
                    NEW_NOTE_REQUEST_CODE -> notesFragment.onDataSetChanged()

                    // Result of taking photo
                    TAKE_PHOTO_REQUEST_CODE -> crop(photoUri)

                    // Result of cropping photo
                    CROP_PHOTO_REQUEST_CODE -> {
                        val intent = Intent(this, NewPhotoNoteActivity::class.java)
                                .putExtra(PHOTO_FILE_KEY, photoFile)
                                .putExtra(PHOTO_URI_KEY, photoUri)
                                .putExtra(CROPPED_PHOTO_URI_KEY, getCroppedPhoto(data))
                        startActivityForResult(intent, NEW_NOTE_REQUEST_CODE)
                    }
                } // when (requestCode)
            }
        } // when (resultCode)
    }

    /**
     * Function for closing a FAB menu for a fragment
     */
    private fun closeFabMenu(): Unit = notesFragment.closeFabMenu()

    /**
     * Function for finishing an ActionMode for the current fragment
     * @param index New index of selected fragment
     */
    private fun finishActionMode(index: Int) {
        // If the data set was changed and notes fragment is visible,
        // then we should reload a list of notes
        /*
        if (needReloadNotes && index == NOTE_FRAGMENT_INDEX) {
            //findFragmentByTag(index).onDataSetChanged()
            needReloadNotes = false
        }*/
    }
}
