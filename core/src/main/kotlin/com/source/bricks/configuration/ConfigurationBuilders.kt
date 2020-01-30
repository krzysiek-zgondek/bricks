package com.source.bricks.configuration

import kotlin.reflect.KClass

/**
 * Creates [Configuration] using [body] lambda as [Configuration.create]
 * implementation.
 * */
@Suppress("UNUSED_PARAMETER")
inline fun <Context : Any, Result> configuration(
    cls: KClass<Context>, crossinline body: Context.() -> Result
): Configuration<Context, Result> {
    return configuration(body)
}

/**
 * Creates [Configuration] using [body] lambda as [Configuration.create]
 * implementation.
 * */
inline fun <Context, Result> configuration(
    crossinline body: Context.() -> Result
): Configuration<Context, Result> {
    return object : Configuration<Context, Result> {
        override fun create(context: Context): Result = body(context)
    }
}

/**
 * Creates [Configuration] using [body] lambda as [Configuration.create]
 * implementation and adds that configuration to provided scope.
 *
 * That scope can be later used to execute all registered [configuration]'s
 * at once.
 * * */
inline fun <Context, Result> configuration(
    scope: ConfigurationScope<Context>?,
    crossinline body: Context.() -> Result
): Configuration<Context, Result> {
    val configuration = configuration(body)
    scope?.add(configuration)
    return configuration
}