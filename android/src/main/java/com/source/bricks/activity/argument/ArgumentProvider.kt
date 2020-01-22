package com.source.bricks.activity.argument

import android.content.Intent
import com.source.core.provider.DelegateProvider
import com.source.core.provider.readOnlyProperty
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


/**
 * For clarity only
 * May be removed in the future
 * */
interface ActivityArgumentProvider<Type> :
    DelegateProvider<Any, ActivityArgument<Type>>


/**
 * Creates provider for later delegation
 * Internal use only
 * */
@PublishedApi
internal inline fun <reified Type> createArgumentProvider(
    name: String? = null,
    crossinline decoder: (Intent, String) -> Type? = ::defaultArgumentDecoder,
    crossinline encoder: (Intent, String, Type) -> Unit = ::defaultArgumentEncoder,
    crossinline default: () -> Type
): ActivityArgumentProvider<Type> {
    return object :
        ActivityArgumentProvider<Type> {
        override fun provideDelegate(
            ref: Any, prop: KProperty<*>
        ): ReadOnlyProperty<Any, ActivityArgument<Type>> {
            val argName = name ?: "arguments.${prop.name}"
            val argument = createArgumentDelegate(argName, decoder, encoder) { default() }

            return readOnlyProperty { _, _ -> argument }
        }
    }
}