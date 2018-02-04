package com.margarita.voicenotes.ui.fragments.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.margarita.voicenotes.common.inflate

/**
 * Base class for all fragments
 */
abstract class BaseFragment: Fragment() {

    @LayoutRes
    protected abstract fun getLayoutRes() : Int

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
            = container?.inflate(getLayoutRes())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }
}