package com.margarita.voicenotes.ui.activities

import android.net.Uri
import android.os.Bundle
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.loadImage
import kotlinx.android.synthetic.main.activity_view_photo.*

class ViewPhotoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_photo)

        // Show fullscreen photo
        val photoUri = intent.getParcelableExtra<Uri>(getString(R.string.photo_intent))
        photoView.loadImage(this, photoUri)
    }
}
