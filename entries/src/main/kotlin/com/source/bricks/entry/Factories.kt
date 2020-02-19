package com.source.bricks.entry

import kotlin.reflect.KParameter


/**
 * If [T] is nullable returns null else returns object instantiated by java [Class.newInstance].
 *
 * You have to provide default constructor for this to work ie.:
 * ```
 * data class Example(val id: Long, val tag: String = "empty") {
 *     constructor() : this(-1L)
 * }
 * ```
 *
 * @throws IllegalStateException if [T] has no default constructor
 * */
inline fun <reified T> javaInstance(): T {
    if (null is T)
        return null as T

    return try {
        T::class.java.newInstance()
    } catch (error: InstantiationException) {
        throw IllegalArgumentException(
                "Provided no default constructor for type: ${T::class.java.name}.",
                error
        )
    }
}

/**
 * If [T] is nullable returns null else returns object instantiated by kotlin constructor reflections.
 * You have to provide constructor with no parameters or all with default values ie.:
 * ```
 * data class Example(val id: Long = -1L, val tag: String = "empty") {
 *     constructor() : this(-1L)
 * }
 * ```

 * @throws IllegalStateException if [T] has no default constructor
 * */
inline fun <reified T> kotlinInstance(): T {
    if (null is T)
        return null as T

    return try {
        val ctr = T::class.constructors.first { it.parameters.all(KParameter::isOptional) }
        ctr.callBy(emptyMap())
    } catch (error: InstantiationException) {
        throw IllegalArgumentException(
                "Provided no default constructor for type: ${T::class.java.name}.",
                error
        )
    }
}
