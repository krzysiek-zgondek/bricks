package com.source.bricks.entry

import kotlin.reflect.KMutableProperty0

inline fun <reified T> KMutableProperty0<T>.remove(): Boolean{
    val delegate = getDelegate() as? EntryProperty<*> ?: return false
    delegate.remove()
    return true
}