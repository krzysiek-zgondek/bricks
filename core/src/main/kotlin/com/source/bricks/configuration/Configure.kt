package com.source.bricks.configuration


/**
 * Wrapper for scope execution if initialization of [ConfigurationScope] instance
 * is not necessary.
 * */
inline fun <reified Context> Context.configure(scope: ConfigurationScope<Context>) {
    scope.configure { this@configure }
}