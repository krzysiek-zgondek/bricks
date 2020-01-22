package com.source.bricks.fragment.argument

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * Gets fragment's arguments bundle or creates and sets new one
 * */
@PublishedApi
internal fun obtainArgumentsBundle(fragment: Fragment): Bundle {
    return fragment.arguments ?: run {
        val newArguments = Bundle()
        fragment.arguments = newArguments
        newArguments
    }
}