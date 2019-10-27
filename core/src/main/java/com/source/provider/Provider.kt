package com.source.provider

/**
 * @project Bricks
 * @author SourceOne on 26.10.2019
 */

/**
 * Definition for a [Provider] [Type]
 */
inline class Provider<in Input, out Output>(
    @PublishedApi
    internal val definition: (Input) -> Output
) {

    /**
     * Invokes definition declared when constructing the [Provider] instance with given
     * [input] parameter [Input]
     * */
    @Suppress("NOTHING_TO_INLINE")
    inline operator fun get(input: Input): Output = definition(input)

    @Suppress("NOTHING_TO_INLINE")
    inline operator fun unaryMinus(): (Input) -> Output = definition
}

///**
// * Creates [Provider] instance using [definition]
// * */
typealias PR<Input, Output> = (Input) -> Output

inline fun <Input, Output> provider(noinline definition: (Input) -> Output): PR<Input, Output> {
    return definition
}

inline operator fun <Input, Output> ((Input) -> Output).get(input: Input): Output = this(input)

///**
// * Creates [Provider] instance using [definition]
// * */
//inline fun <Input, Output> provider(noinline definition: (Input) -> Output): Provider<Input, Output> {
//    return Provider(definition)
//}