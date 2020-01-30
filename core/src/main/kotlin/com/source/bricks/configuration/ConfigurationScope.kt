package com.source.bricks.configuration


/**
 * Configuration scope provides a way to execute many configuration at once.
 *
 * @see ConfigurationListScope as an implementation of a [ConfigurationScope].
 * */
interface ConfigurationScope<Context> {
    fun <Result> add(configuration: Configuration<Context, Result>)
    fun configure(initializer: ConfigurationScope<Context>.() -> Context)
}

