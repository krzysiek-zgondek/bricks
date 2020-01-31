package com.source.bricks.activity.argument

import android.content.Intent

/**
 * This scope enables setting arguments for activity's intent with simple [set] method
 * */
class ActivityArgumentScope(val intent: Intent) {
    /*Future notes
    * when inline classes become stable make this class inline as well
    * */

    /**
     * Saves value into intent using provided [ActivityArgument] description
     * */
    inline fun <reified Type> ActivityArgument<Type>.set(value: Type) {
        encoder(intent, name, value)
    }
}

/**
 * Creates [ActivityArgumentScope] scope within intent
 * */
inline fun Intent.arguments(body: ActivityArgumentScope.() -> Unit): Intent {
    return ActivityArgumentScope(this).apply(body).intent
}