package com.margarita.voicenotes.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.support.annotation.StringRes
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.createImageFile
import com.margarita.voicenotes.common.replace
import com.margarita.voicenotes.common.showCropActivity
import com.margarita.voicenotes.common.showToast
import com.margarita.voicenotes.ui.fragments.base.BaseFragment
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File

/**
 * Base class for all activities
 */
abstract class BaseActivity: AppCompatActivity() {

    companion object {
        private const val FRAGMENT_TAG = "NOTE_FRAGMENT_TAG"
        private const val CONTAINER_ID = R.id.container

        /**
         * Authority for the application's file provider
         */
        private const val FILE_PROVIDER_AUTHORITY
                = "com.margarita.voicenotes.android.fileprovider"

        /**
         * Usual Uri String value for internal photos
         */
        const val COMMON_URI_STRING = "content://$FILE_PROVIDER_AUTHORITY/images/"

        /**
         * Request code for a photo selection
         */
        const val PICK_PHOTO_REQUEST_CODE = 2

        /**
         * Request code for taking photo
         */
        const val TAKE_PHOTO_REQUEST_CODE = 3

        /**
         * Request code for cropping photos
         */
        const val CROP_PHOTO_REQUEST_CODE =  CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE

        /**
         * Keys for Bundle
         */
        const val PHOTO_FILE_KEY = "PHOTO_FILE_KEY"
        const val PHOTO_URI_KEY = "PHOTO_URI_KEY"
        const val CROPPED_PHOTO_URI_KEY = "CROPPED_PHOTO_URI_KEY"
    }

    /**
     * Function for providing a correct navigation to the parent activity
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    //region Helpful function for taking photos
    /**
     * Function which checks if the user's device has apps which can handle intent
     * @param intent Intent which should be handled
     * @return True if intent could be handled, False otherwise
     */
    protected fun checkIntentHandlers(intent: Intent): Boolean
            = intent.resolveActivity(packageManager) != null

    /**
     * Function for creation an intent for taking photos
     */
    protected fun createPhotoIntent(): Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    /**
     * Function for creation a photo file
     */
    protected fun createPhotoFile(): File
            = createImageFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES))

    /**
     * Function for getting a photo Uri from photo file
     */
    protected fun getPhotoUri(photoFile: File): Uri
            = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, photoFile)

    /**
     * Function for showing Activity for taking photos
     */
    protected fun showPhotoActivity(photoIntent: Intent, photoUri: Uri) {
        photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(photoIntent, TAKE_PHOTO_REQUEST_CODE)
    }

    /**
     * Function for removing a photo file
     */
    protected fun deletePhotoFile(photoFile: File?) {
        photoFile?.delete()
    }

    /**
     * Function for showing a crop Activity or an error message
     */
    protected fun crop(photoUri: Uri?) {
        if (photoUri != null) {
            showCropActivity(photoUri)
        } else {
            showToast(R.string.image_loading_error)
        }
    }

    /**
     * Function for getting an Uri of cropped photo
     */
    protected fun getCroppedPhoto(data: Intent?): Uri = CropImage.getActivityResult(data).uri
    //endregion

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

    /**
     * Function for getting an Uri from intent by key
     */
    protected fun getUriFromIntent(key: String): Uri? = intent.getParcelableExtra(key) as Uri?
}