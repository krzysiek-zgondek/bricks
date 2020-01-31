package com.source.bricks.fragment.manager.observer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

inline fun <reified T : Fragment> FragmentManager.registerActiveObserver(
    tag: String? = T::class.java.name, recursive: Boolean = false, crossinline receiver: (T) -> Unit
) {
    val callbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentStarted(fm: FragmentManager, current: Fragment) {
            super.onFragmentStarted(fm, current)
            if (current is T && current.tag == tag) {
                receiver(current)
                fm.unregisterFragmentLifecycleCallbacks(this)
            }
        }
    }

    registerFragmentLifecycleCallbacks(callbacks, recursive)
}

inline fun <reified T : Fragment> FragmentManager.registerInActiveObserver(
    tag: String? = T::class.java.name, recursive: Boolean = false, crossinline receiver: (T) -> Unit
) {
    val callbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentStopped(fm: FragmentManager, current: Fragment) {
            super.onFragmentStopped(fm, current)
            if (current is T && current.tag == tag) {
                receiver(current)
                fm.unregisterFragmentLifecycleCallbacks(this)
            }
        }
    }

    registerFragmentLifecycleCallbacks(callbacks, recursive)
}