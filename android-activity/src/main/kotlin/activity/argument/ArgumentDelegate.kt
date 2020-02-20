package com.source.bricks.activity.argument

import android.app.Activity
import android.content.Intent
import kotlin.reflect.KProperty

/**
 * Creates delegation for argument of type [Type]
 * Internal use only
 * */
@PublishedApi
internal inline fun <reified Type> createArgumentDelegate(
    name: String,
    crossinline decoder: (Intent, String) -> Type? = ::defaultArgumentDecoder,
    crossinline encoder: (Intent, String, Type) -> Unit = ::defaultArgumentEncoder,
    crossinline default: () -> Type
): ActivityArgument<Type> {
    return object : ActivityArgument<Type> {
        override val name: String = name

        override fun getValue(thisRef: Activity, property: KProperty<*>): Type {
            return decoder(thisRef.intent, name) ?: default()
        }

        override fun setValue(thisRef: Activity, property: KProperty<*>, value: Type) {
            encoder(thisRef.intent, name, value)
        }

        override fun decoder(intent: Intent, key: String) =
            decoder(intent, key)

        override fun encoder(intent: Intent, key: String, value: Type) =
            encoder(intent, key, value)
    }
}