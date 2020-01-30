package com.source.bricks.bundle

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

/**
 * @project Bricks
 * @author SourceOne on 21.01.2020
 */

/*
* Default implementation of how to save any data of type [Type] into [Bundle]
*
* This implementation covers most use cases. This should / will be more expanded as need.
* */
inline fun <reified Type> defaultBundleEncoder(bundle: Bundle, key: String, value: Type) {
    when (value) {
        is Serializable -> bundle.putSerializable(key, value)
        is Parcelable -> bundle.putParcelable(key, value)
        else -> throw IllegalArgumentException("cannot serialize unknown type of data")
    }
}