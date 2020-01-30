package com.source.bricks.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

/**
 * Convenience wrappers
 * */
const val wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT
const val matchParent = ViewGroup.LayoutParams.MATCH_PARENT


/**
 * Adds view provided by [provider] to [T].
 * If [override] is set to true [width] and [height] will be set
 * even if view has it's own layoutParams provided.
 *
 * @return created view
 * */
inline fun <T : ViewGroup, R : View> T.add(
    width: Int = wrapContent,
    height: Int = wrapContent,
    override: Boolean = false,
    provider: T.() -> R
): R {
    val view = provider()

    val layoutParams: ViewGroup.LayoutParams? = view.layoutParams
    if (layoutParams != null) {
        if (override) {
            layoutParams.width = width
            layoutParams.height = height
        }

        addView(provider(), layoutParams)
    } else {
        addView(provider(), width, height)
    }

    return view
}

/**
 * Creates view from provided layoutId and adds it to [ViewGroup]
 *
 * @return created view
 * */
inline fun <reified T : View> ViewGroup.include(
    inflater: LayoutInflater = LayoutInflater.from(context),
    @LayoutRes layoutId: Int
): T {
    return inflater.inflate(layoutId, this) as T
}