package com.source.bricks.configuration

import android.content.Context

/**
 * Default context for all configurations created with [contextConfiguration].
 * */
val DefaultContextScope by lazy {
    ConfigurationListScope<Context>()
}