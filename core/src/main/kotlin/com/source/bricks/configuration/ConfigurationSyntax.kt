package com.source.bricks.configuration

/**
 * Runs [Configuration.create] method with supplied [context].
 *
 * For clarity reasons.
 * */
inline fun <Context, reified Result>
        Configuration<Context, out Result>.use(context: Context): Result {
    return create(context)
}