package com.source.bricks.view

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.view.forEach


/**
 * Goes trough all views in [T] and it's hierarchy
 * and executes receiver on them (including parents).
 *
 * Invocation is top-down until last leaf.
 *
 * This implementation uses recursion.
 *
 * @see forEachAll in-place implementation
 * */
fun <T : ViewGroup> T.forEachAllRecursive(receiver: (View) -> Unit) {
    forEach { child ->
        receiver(child)
        if (child is ViewGroup) {
            child.forEachAllRecursive(receiver)
        }
    }
}

/**
 * Goes trough all views in [T] and it's hierarchy
 * and executes receiver on them (including parents).
 *
 * Invocation is top-down until last leaf.
 *
 * This implementation does not use recursion.
 * */
inline fun <T : ViewGroup> T.forEachAll(receiver: (View) -> Unit) {
    var currentNodes = mutableListOf<ViewGroup>(this)

    while (currentNodes.isNotEmpty()) {
        val newNodes = mutableListOf<ViewGroup>()

        currentNodes.forEach { group ->
            group.forEach { view ->
                receiver(view)

                if (view is ViewGroup) {
                    newNodes.add(view)
                }
            }
        }

        currentNodes = newNodes
    }
}

/**
 * Goes trough all views in [T] and it's hierarchy
 * until it finds match with [condition] and returns it
 * or null if no match was found.
 *
 * Invocation is top-down until last leaf.
 *
 * This implementation does use recursion.
 * */
fun <T : ViewGroup> T.findFirstRecursive(condition: (View) -> Boolean): View? {
    forEach { child ->
        if (condition(child))
            return child

        if (child is ViewGroup) {
            val result = child.findFirstRecursive(condition)
            if (result != null)
                return result
        }
    }
    return null
}

/**
 * Goes trough all views in [T] and it's hierarchy
 * until it finds match with [condition] and returns it
 * or null if no match was found.
 *
 * Invocation is top-down until last leaf.
 *
 * This implementation does not use recursion.
 * */
inline fun <T : ViewGroup> T.findFirst(condition: (View) -> Boolean): View? {
    var currentNodes = mutableListOf<ViewGroup>(this)

    while (currentNodes.isNotEmpty()) {
        val newNodes = mutableListOf<ViewGroup>()

        currentNodes.forEach { group ->
            group.forEach { view ->
                if (condition(view))
                    return view

                if (view is ViewGroup) {
                    newNodes.add(view)
                }
            }
        }

        currentNodes = newNodes
    }

    return null
}


/**
 * Applies [receiver] to result of [ViewGroup.findViewById]
 * if view was found or does nothing.
 * */
inline fun <T : View> ViewGroup.onViewById(@IdRes id: Int, receiver: T.() -> Unit): ViewGroup {
    findViewById<T>(id)?.apply(receiver)
    return this
}
