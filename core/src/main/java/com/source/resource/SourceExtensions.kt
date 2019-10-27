package com.source.resource

import com.source.core.Source

/**
 * @project Bricks
 * @author SourceOne on 24.10.2019
 */

/**
 * Creates a resource of type [Type] directly from source
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun <Type> Resource.Companion.from(source: Source<Type>): Resource<Type> {
    return try {
        result(source.get())
    } catch (exception: Exception) {
        error(exception)
    }
}

/**
 * Creates a resource of type [Type] from a source declaration
 * */
inline fun <Type> Resource.Companion.from(declaration: () -> Source<Type>): Resource<Type> {
    return from(declaration())
}

/**
 * Obtains resource [Resource] from source [Source] and tries to cast it as
 * successful entity [T] or throws [TypeCastException]
 * */
inline fun <reified T> Source<Resource<T>>.getOrThrow(): T {
    return (get() as Resource.Data<T>).value
}