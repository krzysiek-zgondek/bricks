package com.source.bricks.fragment.manager

import androidx.annotation.IdRes
import androidx.fragment.app.*

/*
* Think of FragmentManager.switchTo version where you do not need tag
* */
inline fun <reified T : Fragment> FragmentManager.switchTo(
    @IdRes container: Int,
    tag: String,
    backStackAllowed: Boolean = true,
    provider: FragmentTransaction.(T?) -> T
) {
    //search for all currently visible fragments (backstack not included)
    val visibleFragments = fragments.filter { !it.isHidden }

    //find last instance for provided tag and check if it's same type
    val existingFragment = fragments.find { it.tag == tag } as? T

    commit {
        //hide remaining
        hide(visibleFragments)

        val newFragment = provider(existingFragment)
        showOrAdd(container, existingFragment, newFragment, tag)

        if (backStackAllowed)
            addToBackStack(tag)
    }
}

/**
 * Checks if existing fragment is */
@PublishedApi
internal inline fun <reified T : Fragment> FragmentTransaction.showOrAdd(
    container: Int, existingFragment: T?, newFragment: T, tag: String
) {
    if (newFragment == existingFragment)
        show(existingFragment)
    else {
        add(container, newFragment, tag)
    }
}