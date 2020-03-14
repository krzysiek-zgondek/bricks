package com.source.bricks.activity.argument

import android.content.Intent
import android.os.Bundle

/**
 * Default activity's extras tag name for storing arguments bundle
 * */
const val BundleName = "bricks.arguments"

/**
 * Gets activity's arguments bundle or creates and sets new one
 * You can change the name of bundle within activity's extras using
 * @param [name] = [BundleName]
 * */
@PublishedApi
internal fun obtainArgumentsBundle(intent: Intent, name: String = BundleName): Bundle {
    return intent.extras?.getBundle(name) ?: run {
        val newArguments = Bundle()
        intent.putExtra(name, newArguments)
        newArguments
    }
}

/**
 * Updates activity's arguments bundle
 * */
@PublishedApi
internal fun updateArgumentsBundle(
    intent: Intent,
    bundle: Bundle,
    name: String = BundleName
) {
    intent.putExtra(name, bundle)
}