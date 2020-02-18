package com.source.bricks.fragment.transaction.collection

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

/**
 * Hides only those fragments which are not already hidden
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun FragmentTransaction.hide(collection: Collection<Fragment>) {
    collection.forEach { fragment -> internalHide(fragment) }
}

/**
 * Hides only those fragments which are not already hidden
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun FragmentTransaction.hide(vararg fragments: Fragment) {
    fragments.forEach { fragment -> internalHide(fragment) }
}

/**
 * Hides fragment if it's not already hidden
 * Internal use only
 * */
@Suppress("NOTHING_TO_INLINE")
@PublishedApi
internal inline fun FragmentTransaction.internalHide(fragment: Fragment) {
    if (!fragment.isHidden)
        hide(fragment)
}