package com.source.bricks.resource

import android.app.Activity
import android.content.res.Resources
import androidx.fragment.app.Fragment


/**
 * Gets current display width in pixels
 * */
inline val Resources.screenWidth
    get() = displayMetrics.widthPixels

/**
 * Gets current display height in pixels
 * */
inline val Resources.screenHeight
    get() = displayMetrics.heightPixels


/**
 * Gets current display width in pixels
 * */
inline val Activity.screenWidth
    get() = resources.screenWidth

/**
 * Gets current display height in pixels
 * */
inline val Activity.screenHeight
    get() = resources.screenHeight


/**
 * Gets current display width in pixels
 * */
inline val Fragment.screenWidth
    get() = resources.screenWidth

/**
 * Gets current display height in pixels
 * */
inline val Fragment.screenHeight
    get() = resources.screenHeight
