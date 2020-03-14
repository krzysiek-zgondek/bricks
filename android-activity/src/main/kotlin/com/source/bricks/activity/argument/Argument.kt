package com.source.bricks.activity.argument

import android.content.Intent
import androidx.fragment.app.Fragment


/**
 * Creates delegation for intent's argument without default value
 * Argument's value may not be null. When argument is not found
 * @throws IllegalStateException
 * */
inline fun <reified Type> argument(
    name: String? = null,
    crossinline decoder: (Intent, String) -> Type? = ::defaultArgumentDecoder,
    crossinline encoder: (Intent, String, Type) -> Unit = ::defaultArgumentEncoder
): ActivityArgumentProvider<Type> {
    return createArgumentProvider(name, decoder, encoder) {
        throw IllegalStateException("argument $name cannot be null")
    }
}

/**
 * Creates delegation for intent's argument with default value
 * Argument's value may be null
 * */
@Suppress("UNUSED_PARAMETER")
inline fun <reified Type> argument(
    name: String? = null,
    crossinline decoder: (Intent, String) -> Type? = ::defaultArgumentDecoder,
    crossinline encoder: (Intent, String, Type) -> Unit = ::defaultArgumentEncoder,
    default: Type
): ActivityArgumentProvider<Type> {
    return createArgumentProvider(name, decoder, encoder) { default }
}

/**
 * Because you cannot explicitly select Companion objects for receiver as an extenstion
 * functions this method will help you find that wrong implementation is being used
 * */
@Suppress("UNUSED_PARAMETER")
inline fun <reified Type> Fragment.argument(
    name: String? = null,
    crossinline decoder: (Intent, String) -> Type? = ::defaultArgumentDecoder,
    crossinline encoder: (Intent, String, Type) -> Unit = ::defaultArgumentEncoder
): ActivityArgumentProvider<Type> {
    throw IllegalAccessException("Do not use Intent's argument implementation with Fragments. [key = $name]")
}

/**
 * Because you cannot explicitly select Companion objects for receiver as an extenstion
 * functions this method will help you find that wrong implementation is being used
 * */
@Suppress("UNUSED_PARAMETER")
inline fun <reified Type> Fragment.argument(
    name: String? = null,
    crossinline decoder: (Intent, String) -> Type? = ::defaultArgumentDecoder,
    crossinline encoder: (Intent, String, Type) -> Unit = ::defaultArgumentEncoder,
    default: Type
): ActivityArgumentProvider<Type> {
    throw IllegalAccessException("Do not use Intent's argument implementation with Fragments. [key = $name]")
}
