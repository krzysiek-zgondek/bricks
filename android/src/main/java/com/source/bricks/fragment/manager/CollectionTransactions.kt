package com.source.bricks.fragment.manager

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

/**
 * @project Bricks
 * @author SourceOne on 22.01.2020
 */


@Suppress("NOTHING_TO_INLINE")
inline fun FragmentTransaction.add(@IdRes container: Int, collection: Collection<Fragment>) {
    collection.forEach { fragment -> add(container, fragment) }
}

@Suppress("NOTHING_TO_INLINE")
inline fun FragmentTransaction.show(collection: Collection<Fragment>) {
    collection.forEach { fragment -> show(fragment) }
}

@Suppress("NOTHING_TO_INLINE")
inline fun FragmentTransaction.hide(collection: Collection<Fragment>) {
    collection.forEach { fragment -> hide(fragment) }
}
