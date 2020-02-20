package com.source.bricks.fragment.argument

import androidx.fragment.app.Fragment
import com.medizzy.android.libs.bricks.android.arguments.FragmentArgument
import kotlin.reflect.KProperty


/**
 * Creates delegation for argument of type [Type]
 * Internal use only
 * */
@PublishedApi
internal inline fun <reified Type> createArgumentDelegate(
    name: String,
    crossinline decoder: (Fragment, String) -> Type? = ::defaultArgumentDecoder,
    crossinline encoder: (Fragment, String, Type) -> Unit = ::defaultArgumentEncoder,
    crossinline default: () -> Type
): FragmentArgument<Type> {
    return object : FragmentArgument<Type> {
        override val name: String = name

        override fun getValue(thisRef: Fragment, property: KProperty<*>): Type {
            return decoder(thisRef, name) ?: default()
        }

        override fun setValue(thisRef: Fragment, property: KProperty<*>, value: Type) {
            encoder(thisRef, name, value)
        }
    }
}