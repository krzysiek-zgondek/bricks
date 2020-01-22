package com.source.bricks.activity.argument

import android.app.Activity
import android.content.Intent
import com.source.core.argument.Argument

/**
 * Activity's base type of argument
 * */
interface ActivityArgument<Type> : Argument<Activity, Type>{
    fun decoder(intent: Intent, key: String): Type?
    fun encoder(intent: Intent, key: String, value: Type)
}