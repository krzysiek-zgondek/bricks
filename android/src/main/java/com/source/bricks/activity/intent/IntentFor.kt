package com.source.bricks.activity.intent

import android.app.Activity
import android.content.Context
import android.content.Intent

/**
 * Simple wrapper for creating [Intent] and applying changes to it right away
 * */
inline fun <reified IntentActivity : Activity> Context.intentFor(configuration: Intent.() -> Unit): Intent {
    return Intent(this, IntentActivity::class.java).apply(configuration)
}