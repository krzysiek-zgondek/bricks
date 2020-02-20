package com.source.bricks.entry.scope

import com.source.bricks.entry.EntryDescriptor
import com.source.bricks.reflect.tClass


/**
 * Forwards T class implementation
 * */
inline fun <reified T> EntryScope.set(descriptor: EntryDescriptor<T>, value: T) {
    set(descriptor, value, T::class.tClass)
}

/**
 * Forwards T class implementation
 * */
inline fun <reified T> EntryScope.get(descriptor: EntryDescriptor<T>): T? {
    return get(descriptor, T::class.tClass)
}