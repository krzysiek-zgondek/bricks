package com.source.core

/**
 * @project Bricks
 * @author Source - Krzysztof Zgondek on 24.10.2019
 */

/**
 * Class which describes how to receive an objects of type [Type]
 */
inline class Source<out Type>(
    @PublishedApi
    internal val path: () -> Type
) {
    /**
     * Executes definition declared when constructing the [Source] instance
     * */
    @Suppress("NOTHING_TO_INLINE")
    inline fun get(): Type = path()

    @Suppress("NOTHING_TO_INLINE")
    inline operator fun unaryMinus(): Type {
        return get()
    }
}