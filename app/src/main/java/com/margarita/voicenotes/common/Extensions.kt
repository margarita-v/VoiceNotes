package com.margarita.voicenotes.common

import android.content.Context
import android.support.annotation.StringRes
import android.widget.Toast

/**
 * Function for showing a simple Toast message
 * @param messageRes String resource ID for a message text
 * @param duration Duration of the Toast message
 */
fun Context.showToast(@StringRes messageRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, messageRes, duration).show()
}
