package com.margarita.voicenotes.ui.activities

import android.support.v7.app.AppCompatActivity
import com.margarita.voicenotes.R
import com.margarita.voicenotes.common.replace
import com.margarita.voicenotes.ui.fragments.base.BaseFragment

/**
 * Base class for all activities
 */
abstract class BaseActivity: AppCompatActivity() {

    private companion object {
        const val FRAGMENT_TAG = "NOTE_FRAGMENT_TAG"
        const val CONTAINER_ID = R.id.container
    }

    /**
     * Function for providing a correct navigation to the parent activity
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

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
}