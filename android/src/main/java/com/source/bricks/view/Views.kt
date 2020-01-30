package com.source.bricks.view

import android.view.View


/**
 * Visibility property of [View].
 * If true view is in state [View.VISIBLE]
 * If false view is in state [View.GONE]
 * */
inline var <reified T : View> T.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

/**
 * Sets [visible] property on group of views provided in [views]
 * using provided [condition] lambda.
 *
 * @see View.visible property
 * */
inline fun visible(vararg views: View, condition: (View) -> Boolean) {
    views.forEach { view ->
        view.visible = condition(view)
    }
}
