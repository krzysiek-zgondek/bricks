package com.source.bricks.koin

import com.source.bricks.configuration.Configuration
import com.source.bricks.configuration.configuration
import org.koin.core.scope.Scope


/*
* Wrapper for convenient use with Koin library modules
* */
inline fun <reified Result> koinConfiguration(
    crossinline factory: Scope.() -> Result
): Configuration<Scope, Result> {
    return configuration(factory)
}