package com.margarita.voicenotes.ui.activities

import android.content.Intent
import android.os.Parcelable
import android.provider.MediaStore
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.replace
import com.margarita.voicenotes.ui.fragments.base.BaseFragment

/**
 * Base class for all activities
 */
abstract class BaseActivity: AppCompatActivity() {

    companion object {
        private const val FRAGMENT_TAG = "NOTE_FRAGMENT_TAG"
        private const val CONTAINER_ID = R.id.container

        /**
         * Request code for a photo selection
         */
        const val PICK_PHOTO_REQUEST_CODE = 2
    }

    /**
     * Function for providing a correct navigation to the parent activity
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * Function for creation an intent for taking photos
     */
    protected fun createPhotoIntent(): Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    /**
     * Function for restoring the last fragment from the fragment manager
     */
    protected fun restoreFragment(): BaseFragment?
            = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as BaseFragment?

    /**
     * Function for setting the fragment to the activity
     */
    protected fun setFragment(fragment: BaseFragment): Unit
            = supportFragmentManager.replace(CONTAINER_ID, fragment, FRAGMENT_TAG)

    /**
     * Function for getting a parcelable extra from intent
     * @param intentRes String resource ID for intent's name
     */
    protected fun getParcelableExtra(@StringRes intentRes: Int)
            = intent.getParcelableExtra(getString(intentRes)) as Parcelable
}