package com.source.bricks.activity.argument

import android.app.Activity
import com.source.bricks.activity.intent.extraGet
import com.source.bricks.activity.intent.extraSet
import com.source.core.argument.Argument
import com.source.core.provider.readOnlyProperty
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


/**
 * Activity's base type of argument
 * */
interface ActivityArgument<Type> :
    Argument<Activity, Type>

/**
 * Creates delegation for activity's argument with default value
 * Argument's value may be null
 * */
inline fun <reified Type> argument(default: Type, name: String? = null):
        ActivityArgumentProvider<Type> {
    return createArgumentProvider(name) { default }
}

/**
 * Creates delegation for activity's argument with default value
 * Argument's value may be null
 * */
inline fun <reified Type> argument(name: String? = null):
        ActivityArgumentProvider<Type> {
    return createArgumentProvider(name) {
        throw IllegalStateException("argument cannot be null")
    }
}

/**
 * Creates provider for later delegation
 * */
@PublishedApi
internal inline fun <reified Type> createArgumentProvider(
    name: String? = null, crossinline default: () -> Type
): ActivityArgumentProvider<Type> {
    return object :
        ActivityArgumentProvider<Type> {
        override fun provideDelegate(
            ref: Any, prop: KProperty<*>
        ): ReadOnlyProperty<Any, ActivityArgument<Type>> {
            val argName = name ?: "arguments.${prop.name}"
            val argument = createArgumentDelegate(argName) { default() }

            return readOnlyProperty { _, _ -> argument }
        }
    }
}

/**
 * Creates delegation for argument of type [Type]
 * */
@PublishedApi
internal inline fun <reified Type> createArgumentDelegate(
    name: String,
    crossinline default: () -> Type
): ActivityArgument<Type> {
    return object : ActivityArgument<Type> {
        override val name: String = name

        override fun getValue(thisRef: Activity, property: KProperty<*>): Type {
            return thisRef.intent.extraGet<Type>(name) ?: default()
        }

        override fun setValue(thisRef: Activity, property: KProperty<*>, value: Type) {
            thisRef.intent.extraSet(name, value)
        }
    }
}


