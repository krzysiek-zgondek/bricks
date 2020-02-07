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
 * If the type [T] doesn't match the type found it throws [ClassCastException]
 *
 * @see onViewByIdChecked for the implementation which doesn't throw if different type is found
 *
 * @throws ClassCastException
 * @return provided [ViewGroup]
 * */
inline fun <T : View> ViewGroup.onViewById(@IdRes id: Int, receiver: T.() -> Unit): ViewGroup {
    findViewById<T>(id)?.apply(receiver)
    return this
}


/**
 * Like [onViewById] it looks for first instance of view with corresponding id, but it also checks
 * if type correct. If not it looks deeper into hierarchy until view is found or do nothing.
 * Uses [findFirstRecursive] to find corresponding view
 *
 * @return provided [ViewGroup]
 * */
inline fun <reified T : View> ViewGroup.onViewByIdChecked(@IdRes id: Int, receiver: T.() -> Unit): ViewGroup {
    val view = findFirstRecursive { view -> view.id == id && view is T } as? T
    view?.receiver()
    return this
}