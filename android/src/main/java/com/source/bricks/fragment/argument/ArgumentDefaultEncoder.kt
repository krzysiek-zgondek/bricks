package com.source.bricks.fragment.argument

import androidx.fragment.app.Fragment
import com.source.bricks.bundle.defaultBundleEncoder

/**
 * Saves value of name [key] into [Fragment]'s arguments bundle
 * */
inline fun <reified Type> defaultArgumentEncoder(fragment: Fragment, key: String, value: Type) {
    defaultBundleEncoder(obtainArgumentsBundle(fragment), key, value)
}