package com.source.bricks.fragment.manager.content

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.source.bricks.fragment.manager.observer.registerActiveObserver

/**
 * Executes action when fragment described in [descriptor] become
 * active which means it is in started state and safe to use
 * */
inline fun <reified T : Fragment> FragmentManager.whenAvailable(
    descriptor: FragmentDescriptor<T>,
    crossinline action: (T) -> Unit
) {
    registerActiveObserver<T>(descriptor.tag) { fragment ->
        action(fragment)
    }
}

/**
 * Executes action on any found fragment described in [result].
 * Fragment has to be already active and safe to use.
 *
 * To find fragment in [FragmentManager.getFragments] it uses
 * [SwitchFilters.filterClass] filter which means it looks for
 * match only in type. If you want to check for tag use [whenActiveTag]
 * */
inline fun <reified T : Fragment> FragmentManager.whenActiveClass(
    descriptor: FragmentDescriptor<T>,
    action: (List<T>) -> Unit
) {
    val fragments =
        SwitchFilters.filterClass(this, descriptor)
    if (fragments.isNotEmpty())
        action(fragments)
}

/**
 * Executes action on any found fragment described in [result].
 * Fragment has to be already active and safe to use.
 *
 * To find fragment in [FragmentManager.getFragments] it uses
 * [SwitchFilters.filterTag] filter which means it looks for
 * match only in tag. If you want to check for class use [whenActiveClass]
 * */
inline fun <reified T : Fragment> FragmentManager.whenActiveTag(
    descriptor: FragmentDescriptor<T>,
    action: (List<Fragment>) -> Unit
) {
    val fragments =
        SwitchFilters.filterTag(this, descriptor)
    if (fragments.isNotEmpty())
        action(fragments)
}