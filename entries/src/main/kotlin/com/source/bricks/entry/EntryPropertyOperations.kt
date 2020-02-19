package com.source.bricks.entry

import kotlin.reflect.KMutableProperty0


/**
 * Checks if provided [KMutableProperty0] is of type [EntryProperty] and if so
 * executes [EntryProperty.remove].
 * */
inline fun <reified T> KMutableProperty0<T>.remove(): Boolean{
    val delegate = getDelegate() as? EntryProperty<*> ?: return false
    delegate.remove()
    return true
}