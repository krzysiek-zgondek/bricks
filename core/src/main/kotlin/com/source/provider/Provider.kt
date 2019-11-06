package com.source.provider

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
inline fun <Id, Type1, Type2> provider(
    crossinline init: (Id) -> Type1?,
    crossinline next: (Id, Type1?) -> Type2
): (Id) -> Type2 {
    return { input: Id ->
        val first = init(input)
        execute(input, first, next)
    }
}

/**
 * Creates provider that uses [context] object as a receiver
 * for both [init] and [next] step lambdas
 * [init] - creates initial value for [Id] input
 * [next] - provides value that depends on [Id] input
 * and receives value returned by [init] invocation
 * [regular provider][regular]
 *
 * */
inline fun <Id, Type1, Type2, Context> provider(
    context: Context,
    crossinline init: Context.(Id) -> Type1?,
    crossinline next: Context.(Id, Type1?) -> Type2
): (Id) -> Type2 {
    return { input: Id ->
        val first = init(context, input)
        execute(input, context, first, next)
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
inline fun <Id, Type1, Type2, Context> provider(
    context: Context,
    crossinline init: Context.(Id) -> Type1,
    crossinline next: Context.(Id, Type1) -> Type2,
    crossinline exit: Context.(Id, Type1, Type2) -> Unit
): Provider<Id, Type2> {
    return Provider { input: Id ->
        val first = init(context, input)
        execute(input, context, first, next).also { result ->
            exit(context, input, first, result)
        }
    }
}

/**
 * Inlines [next] with specified values [input], [init]
 *
 * */
@PublishedApi
internal inline fun <Id, Type1, Type2> execute(
    input: Id,
    init: Type1,
    crossinline next: (Id, Type1) -> Type2
): Type2 {
    return next(input, init)
}

/**
 * Inlines [next] with specified values [input], [init]
 * and wraps it with [context] object
 *
 * */
@PublishedApi
internal inline fun <Id, Type1, Type2, Context> execute(
    input: Id,
    context: Context,
    init: Type1,
    crossinline next: Context.(Id, Type1) -> Type2
): Type2 {
    return next(context, input, init)
}