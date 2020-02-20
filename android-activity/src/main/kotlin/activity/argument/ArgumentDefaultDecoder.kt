package com.source.bricks.activity.argument

import android.content.Intent
import com.source.bricks.bundle.defaultBundleDecoder

/**
 * Extracts value of name [key] from [Intent]'s arguments bundle
 * */
inline fun <reified Type> defaultArgumentDecoder(intent: Intent, key: String): Type? {
    return defaultBundleDecoder(obtainArgumentsBundle(intent), key)
}