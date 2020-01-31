package com.source.resource

import com.source.bricks.Source

/**
 * @project Bricks
 * @author SourceOne on 24.10.2019
 */

/**
 * Creates a resource of type [Type] directly from source
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun <Type> Resource.Companion.from(source: Source<Type>): Resource<Type> {
    return resource {
        source.get()
    }
}

/**
 * Creates a resource of type [T] directly from source
 * and executes transformation on that source
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun <T, R> Resource.Companion.from(
    source: Source<T>,
    transformation: (T) -> R
): Resource<R> {
    return resource {
        val result = source.get()
        transformation(result)
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