package com.margarita.voicenotes.ui.activities

import android.os.Bundle
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.loadImage
import com.margarita.voicenotes.common.parseStringToUri
import com.margarita.voicenotes.models.entities.NoteItem
import kotlinx.android.synthetic.main.activity_view_note.*

class ViewNoteActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_note)
        val noteItem = intent.getParcelableExtra(getString(R.string.note_intent)) as NoteItem
        showNoteInfo(noteItem)
    }

    /**
     * Function for showing the note's info
     * @param noteItem Note which info will be shown
     */
    private fun showNoteInfo(noteItem: NoteItem) {
        tvDescription.text = noteItem.description
        tvDate.text = noteItem.parseDate()
        ivPhoto.loadImage(this, noteItem.croppedPhotoUri?.parseStringToUri())
    }
}
