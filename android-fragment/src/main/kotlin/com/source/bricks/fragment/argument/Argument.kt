package com.source.bricks.fragment.argument

import androidx.fragment.app.Fragment

/**
 * Creates delegation for fragment's argument without default value
 * Argument's value may not be null. When argument is not found
 * @throws IllegalStateException
 * */
inline fun <reified Type> Fragment.argument(
    name: String? = null,
    crossinline decoder: (Fragment, String) -> Type? = ::defaultArgumentDecoder,
    crossinline encoder: (Fragment, String, Type) -> Unit = ::defaultArgumentEncoder
): FragmentArgumentProvider<Type> {
    return createArgumentProvider(name, decoder, encoder) {
        throw IllegalStateException("argument $name cannot be null")
    }
}

/**
 * Creates delegation for fragment's argument with default value
 * Argument's value may be null
 * */
inline fun <reified Type> Fragment.argument(
    name: String? = null,
    crossinline decoder: (Fragment, String) -> Type? = ::defaultArgumentDecoder,
    crossinline encoder: (Fragment, String, Type) -> Unit = ::defaultArgumentEncoder,
    default: Type
): FragmentArgumentProvider<Type> {
    return createArgumentProvider(name, decoder, encoder) {
        default
    }
}