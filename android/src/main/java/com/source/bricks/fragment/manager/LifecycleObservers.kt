package com.source.bricks.fragment.manager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

inline fun <T : Fragment> FragmentManager.registerCreateObserver(
    fragment: T, recursive: Boolean = false, crossinline receiver: () -> Unit
) {
    val callbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager, current: Fragment, v: View, savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, current, v, savedInstanceState)
            if (fragment == current) {
                receiver()
                fm.unregisterFragmentLifecycleCallbacks(this)
            }
        }
    }

    registerFragmentLifecycleCallbacks(callbacks, recursive)
}

inline fun <T : Fragment> FragmentManager.registerDestroyObserver(
    fragment: T, recursive: Boolean = false, crossinline receiver: () -> Unit
) {
    val callbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewDestroyed(fm: FragmentManager, current: Fragment) {
            super.onFragmentViewDestroyed(fm, current)
            if (fragment == current) {
                receiver()
                fm.unregisterFragmentLifecycleCallbacks(this)
            }
        }
    }

    registerFragmentLifecycleCallbacks(callbacks, recursive)
}