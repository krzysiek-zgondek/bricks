package com.source.bricks.integration.android

import android.content.Context
import com.source.bricks.configuration.Configuration
import com.source.bricks.configuration.ConfigurationScope
import com.source.bricks.configuration.configuration


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