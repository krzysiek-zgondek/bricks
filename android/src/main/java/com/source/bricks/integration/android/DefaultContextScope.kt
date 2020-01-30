package com.source.bricks.integration.android

import android.content.Context
import com.source.bricks.configuration.ConfigurationListScope

/**
 * Default context for all configurations created with [contextConfiguration].
 * */
val DefaultContextScope by lazy {
    ConfigurationListScope<Context>()
}