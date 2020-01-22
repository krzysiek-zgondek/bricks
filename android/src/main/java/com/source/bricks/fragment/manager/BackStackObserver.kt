package com.source.bricks.fragment.manager

import androidx.fragment.app.FragmentManager

inline fun FragmentManager.registerBackStackObserver(crossinline receiver: (String?) -> Unit) {
    addOnBackStackChangedListener {
        if (backStackEntryCount > 0) {
            receiver(
                getBackStackEntryAt(backStackEntryCount - 1).name
            )
        }
    }
}