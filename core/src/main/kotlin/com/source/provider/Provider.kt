package com.source.provider

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * @project Bricks
 * @author SourceOne on 26.10.2019
 */

/**
 * Definition for a [Provider]
 *
 *
 * !!this will probably be removed
 *
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

/**
 * Handy shortcut that makes the call to provider look like
 * normal table lookup
 * */
@Suppress("NOTHING_TO_INLINE")
inline operator fun <Input, Output> ((Input) -> Output).get(input: Input): Output = this(input)

/**
 * Creates simplest provider
 * */
inline fun <Id, Type1, Type2> provide(
    crossinline init: (Id) -> Type1,
    crossinline next: (Id, Type1) -> Type2
): (Id) -> Type2 {
    return { input: Id ->
        val first = init(input)
        next(input, first)
    }
}

/**
 * Creates provider that uses [context] object as a receiver
 * for both [init] and [next] step lambdas
 * [init] - creates initial value for [Id] input
 * [next] - provides value that depends on [Id] input
 * and receives value returned by [init] invocation
 *
 * */
inline fun <Id, Type1, Type2, Context> provide(
    context: Context,
    crossinline init: Context.(Id) -> Type1,
    crossinline next: Context.(Id, Type1) -> Type2
): (Id) -> Type2 {
    return { input: Id ->
        val first = init(context, input)
        next(context, input, first)
    }
}

/**
 * Creates provider that uses [context] object as a receiver
 * for both [init] and [next] step lambdas
 * [init] - creates initial value for [Id] input
 * [next] - provides value that depends on [Id] input
 * and receives value returned by [init] invocation.
 *
 * After all the evaluation [exit] block is called.
 *
 * This method is useful if you want to perform some
 * operation after providing actual value because
 * [exit] block do not modify output of [init] block
 *
 * */


object Async

inline fun <Id, Type1, Type2> Async.provide(
    crossinline init: suspend (Id) -> Type1,
    crossinline next: suspend (Id, Type1) -> Type2
): suspend (Id) -> Type2 {
    return { input: Id ->
        val first = init(input)
        next(input, first)
    }
}

inline fun <Id, Type1, Type2, Context> Async.provide(
    context: Context,
    crossinline init: suspend Context.(Id) -> Type1,
    crossinline next: suspend Context.(Id, Type1) -> Type2
): suspend (Id) -> Type2 {
    return { input: Id ->
        val first = init(context,input)
        next(context, input, first)
    }
}

