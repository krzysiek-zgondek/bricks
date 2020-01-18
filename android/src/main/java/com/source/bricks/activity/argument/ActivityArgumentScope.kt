package com.source.bricks.activity.argument

import android.app.Activity
import android.content.Intent
import com.source.bricks.activity.intent.extraSet
import com.source.core.argument.Argument

/**
 * This scope enables setting arguments for activity's intent with simple [set] method
 * */
class ActivityArgumentScope(val intent: Intent){
    fun <Type> Argument<Activity, Type>.set(value: Type) {
        intent.extraSet(name, value)
    }
}

/**
 * Creates [ActivityArgumentScope] scope within intent
 * */
inline fun Intent.arguments(body: ActivityArgumentScope.() -> Unit): Intent {
    return ActivityArgumentScope(this).apply(body).intent
}