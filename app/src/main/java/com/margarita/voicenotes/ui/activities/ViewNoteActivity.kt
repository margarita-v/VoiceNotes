package com.margarita.voicenotes.ui.activities

import android.content.Intent
import android.os.Bundle
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.*
import com.margarita.voicenotes.models.entities.NoteItem
import kotlinx.android.synthetic.main.activity_view_note.*

class ViewNoteActivity : BaseActivity() {

    /**
     * Intent for viewing a fullscreen photo
     */
    private val photoIntent by lazy {
        Intent(this@ViewNoteActivity, ViewPhotoActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_note)
        val noteItem = getParcelableExtra(R.string.note_intent) as NoteItem
        showNoteInfo(noteItem)
    }

    /**
     * Function for showing the note's info
     * @param noteItem Note which info will be shown
     */
    private fun showNoteInfo(noteItem: NoteItem) {
        tvDescription.text = noteItem.description
        tvCategory.setCategoryName(getCategoryName(noteItem))
        tvDate.text = noteItem.parseDate()
        val photoUri = noteItem.photoUri?.parseStringToUri()
        if (photoUri != null) {
            ivPhoto.loadImage(this, photoUri)
            ivPhoto.setOnClickListener {
                startActivity(photoIntent.putExtra(getString(R.string.photo_intent), photoUri))
            }
        } else {
            tvEmptyPhoto.show()
        }
    }
}
