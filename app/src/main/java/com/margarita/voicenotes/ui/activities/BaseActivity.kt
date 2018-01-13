package com.margarita.voicenotes.ui.activities

import android.support.v7.app.AppCompatActivity

/**
 * Base class for all activities
 */
abstract class BaseActivity: AppCompatActivity() {

    /**
     * Function for providing a correct navigation to the parent activity
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}