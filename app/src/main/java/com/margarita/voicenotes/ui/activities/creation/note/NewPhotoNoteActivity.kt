package com.margarita.voicenotes.ui.activities.creation.note

import android.os.Bundle
import java.io.File

class NewPhotoNoteActivity: NewNoteActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val photoFile = intent.getSerializableExtra(PHOTO_FILE_KEY) as File?
        if (photoFile != null) {
            newNoteFragment.photoFile = photoFile
            newNoteFragment.photoUri = getUriFromIntent(PHOTO_URI_KEY)
            newNoteFragment.croppedPhotoUri = getUriFromIntent(CROPPED_PHOTO_URI_KEY)

            // Clear intent's fields
            intent.removeExtra(PHOTO_FILE_KEY)
            intent.removeExtra(PHOTO_URI_KEY)
            intent.removeExtra(CROPPED_PHOTO_URI_KEY)
        }
    }

    override fun usedForCreation(): Boolean = false
}