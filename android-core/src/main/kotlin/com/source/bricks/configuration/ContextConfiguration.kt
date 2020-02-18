package com.source.bricks.configuration

import android.content.Context


/*
* Wrapper for convenient use with Android platform
* */
inline fun <reified Result> contextConfiguration(
    scope: ConfigurationScope<Context> = DefaultContextScope,
    crossinline factory: Context.() -> Result
): Configuration<Context, Result> {
    return configuration(scope, factory)
}

/*
* Wrapper for convenient use with Android platform
* */
inline fun <reified Result> contextConfiguration(
    crossinline factory: Context.() -> Result
): Configuration<Context, Result> {
    return configuration(factory)
}