package com.source.bricks.fragment.transaction.collection

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

/**
 * Version of [FragmentTransaction.show] for any [Collection]
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun FragmentTransaction.show(collection: Collection<Fragment>) {
    collection.forEach { fragment ->
        show(fragment)
    }
}

/**
 * Version of [FragmentTransaction.show] for any length of [fragments]
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun FragmentTransaction.show(vararg fragments: Fragment) {
    fragments.forEach { fragment ->
        show(fragment)
    }
}