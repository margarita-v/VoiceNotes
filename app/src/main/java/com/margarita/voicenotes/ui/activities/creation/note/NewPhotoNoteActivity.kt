package com.margarita.voicenotes.ui.activities.creation.note

import android.os.Bundle
import java.io.File

class NewPhotoNoteActivity: NewNoteActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (photoFile == null) {
            photoFile = intent.getSerializableExtra(PHOTO_FILE_KEY) as File?
            newNoteFragment.photoUri = getUriFromIntent(PHOTO_URI_KEY)
            newNoteFragment.croppedPhotoUri = getUriFromIntent(CROPPED_PHOTO_URI_KEY)
        }
    }

    override fun usedForCreation(): Boolean = false
}