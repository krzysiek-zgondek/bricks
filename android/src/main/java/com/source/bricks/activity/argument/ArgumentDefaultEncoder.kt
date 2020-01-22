package com.source.bricks.activity.argument

import android.content.Intent
import com.source.bricks.bundle.defaultBundleEncoder

/**
 * Saves value of name [key] into [Intent]'s arguments bundle
 * */
inline fun <reified Type> defaultArgumentEncoder(intent: Intent, key: String, value: Type) {
    return defaultBundleEncoder(obtainArgumentsBundle(intent), key, value)
}