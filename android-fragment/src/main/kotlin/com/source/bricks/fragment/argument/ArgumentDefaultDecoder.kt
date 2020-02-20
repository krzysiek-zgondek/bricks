package com.source.bricks.fragment.argument

import androidx.fragment.app.Fragment
import com.source.bricks.bundle.defaultBundleDecoder

/**
 * Extracts value of name [key] from [Fragment]'s arguments bundle
 * */
inline fun <reified Type> defaultArgumentDecoder(fragment: Fragment, key: String): Type? {
    return defaultBundleDecoder(obtainArgumentsBundle(fragment), key)
}