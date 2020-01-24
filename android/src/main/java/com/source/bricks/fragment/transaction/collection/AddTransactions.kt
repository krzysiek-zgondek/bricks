package com.source.bricks.fragment.transaction.collection

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

/**
 * @project Bricks
 * @author SourceOne on 22.01.2020
 */

/**
 * Adds all [fragments] to the [FragmentTransaction]
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun FragmentTransaction.add(@IdRes container: Int, vararg fragments: Fragment) {
    fragments.forEach { fragment ->
        add(container, fragment)
    }
}

/**
 * Adds all [pairs]'s fragments to [FragmentTransaction]
 * Second component of [Pair] is fragment's tag
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun FragmentTransaction.add(@IdRes container: Int, vararg pairs: Pair<Fragment, String?>) {
    pairs.forEach { (fragment, tag) ->
        add(container, fragment, tag)
    }
}