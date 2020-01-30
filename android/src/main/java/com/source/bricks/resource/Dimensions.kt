package com.source.bricks.resource

import android.app.Activity
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment


/**
 * Retrieves [resId] dimension from [Resources]
 * @param resId - id of the dimension
 * @return dimension in pixels scaled with [DisplayMetrics]
 *
 * This is a wrapper for more convenient use.
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun Resources.dimen(@DimenRes resId: Int): Int {
    return getDimensionPixelSize(resId)
}

/**
 * Calculates pixels from dp units [TypedValue.COMPLEX_UNIT_DIP]
 * @value dimension in dp
 * @return dimension converted to px [Float]
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun Resources.dp(value: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, displayMetrics)
}

/**
 * Calculates pixels from dp units [TypedValue.COMPLEX_UNIT_DIP]
 * @value dimension in dp
 * @return dimension converted to px [Int]
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun Resources.dpi(value: Float): Int {
    return dp(value).toInt()
}


/**
 * Retrieves [resId] dimension from activity's [Resources]
 * @param resId - id of the dimension
 * @return dimension in pixels scaled with [DisplayMetrics]
 *
 * This is a wrapper for more convenient use.
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun Activity.dimen(@DimenRes resId: Int): Int {
    return resources.dimen(resId)
}

/**
 * Calculates pixels from dp units [TypedValue.COMPLEX_UNIT_DIP]
 * @value dimension in dp
 * @return dimension converted to px [Float]
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun Activity.dp(value: Float): Float {
    return resources.dp(value)
}

/**
 * Calculates pixels from dp units [TypedValue.COMPLEX_UNIT_DIP]
 * @value dimension in dp
 * @return dimension converted to px [Int]
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun Activity.dpi(value: Float): Int {
    return resources.dpi(value)
}


/**
 * Retrieves [resId] dimension from fragments's [Resources]
 * @param resId - id of the dimension
 * @return dimension in pixels scaled with [DisplayMetrics]
 *
 * This is a wrapper for more convenient use.
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun Fragment.dimen(@DimenRes resId: Int): Int {
    return resources.dimen(resId)
}

/**
 * Calculates pixels from dp units [TypedValue.COMPLEX_UNIT_DIP]
 * @value dimension in dp
 * @return dimension converted to px [Float]
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun Fragment.dp(value: Float): Float {
    return resources.dp(value)
}


/**
 * Calculates pixels from dp units [TypedValue.COMPLEX_UNIT_DIP]
 * @value dimension in dp
 * @return dimension converted to px [Int]
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun Fragment.dpi(value: Float): Int {
    return resources.dpi(value)
}
