package com.source.bricks.fragment.argument

import androidx.fragment.app.Fragment
import com.medizzy.android.libs.bricks.android.arguments.FragmentArgument
import kotlin.reflect.KProperty

/**
 * @project Bricks
 * @author SourceOne on 21.01.2020
 */

interface FragmentArgumentProvider<Type> {
    operator fun provideDelegate(
        ref: Fragment, prop: KProperty<*>
    ): FragmentArgument<Type>
}

/**
 * Creates provider for later delegation
 * Internal use only
 * */
@PublishedApi
internal inline fun <reified Type> createArgumentProvider(
    name: String? = null,
    crossinline decoder: (Fragment, String) -> Type? = ::defaultArgumentDecoder,
    crossinline encoder: (Fragment, String, Type) -> Unit = ::defaultArgumentEncoder,
    crossinline default: () -> Type
): FragmentArgumentProvider<Type> {
    return object : FragmentArgumentProvider<Type> {
        override fun provideDelegate(ref: Fragment, prop: KProperty<*>): FragmentArgument<Type> {
            val argName = name ?: "arguments.${prop.name}"

            return createArgumentDelegate(argName, decoder, encoder) {
                default()
            }
        }
    }
}