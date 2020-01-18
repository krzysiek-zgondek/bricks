package com.source.bricks.activity.intent

import android.content.Intent
import android.os.Parcelable
import java.io.Serializable


/**
 * Obtains extra parameter from intent and casts it to proper type or returns null
 * */
inline fun <reified Type> Intent.extraGet(key: String): Type? {
    return extras?.run { get(key) as? Type }
}

/**
 * Sets extra intent's parameter or @throws [IllegalArgumentException] if the type
 * is not supported
 * */
fun <Type> Intent.extraSet(key: String, value: Type) {
    //todo could include converter factory as third parameter to try converting unknown type
    //before throwing

    when (value) {
        is Serializable -> putExtra(key, value)
        is Parcelable -> putExtra(key, value)
        else -> throw IllegalArgumentException("cannot serialize unknown type of data")
    }
}