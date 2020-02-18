package com.source.bricks.reflect

import kotlin.reflect.KClass

/**
 * Combines kotlin [KClass] and nullability of type [T]
 * */
@Suppress("UPPER_BOUND_VIOLATED")
data class TClass<T> @PublishedApi internal constructor(
        val isNullable: Boolean,
        val kClass: KClass<T>
)

/**
 * Returns [TClass] wrapper for [KClass] either type [T] is nullable or not.
 * This getter violates upper bound of [KClass] parameter but underlying implementation is same
 * for both T and T?. There are specific use cases when this might be handful ie.
 * if you handle nullability yourself and want to stay platform agnostic and still use KClass instead
 * of ie. [Class]
 * */
@Suppress("UPPER_BOUND_VIOLATED")
inline val <reified T> KClass<T>.tClass: TClass<T>
    get() = TClass(null is T, this)

/**
 * Returns proper [java] casting. Without wrapping throws [kotlin.jvm.internal.ClassBasedDeclarationContainer].
 * */
inline val <T> TClass<T>.java: Class<T>
    get() = kClass.java


/**
 * Copied from kotlin.reflect.full instead of applying 2.5mb of unused code
 *
 * Casts the given [value] to the class represented by this [KClass] object.
 * Returns `null` if the value is `null` or if it is not an instance of this class.
 *
 * @see [KClass.safeCast] from kotlin.reflect.full
 */
@Suppress("UNCHECKED_CAST")
fun <T> TClass<T>.safeCast(value: Any?): T? {
    return if (kClass.isInstance(value)) value as T else null
}