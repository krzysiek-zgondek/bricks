package com.source.bricks.entry.scope

import com.source.bricks.entry.EntryDescriptor
import com.source.bricks.entry.EntryProperty
import com.source.bricks.entry.define
import com.source.bricks.entry.entry
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

/**
 * Scoped version of [entry] factory method
 * */
inline fun <reified T> EntryScope.register(): EntryProperty<T> {
    return register() { define<T>() }
}

/**
 * Scoped version of [entry] factory method
 * */
inline fun <reified T> EntryScope.register(
    definition: () -> EntryDescriptor<out T>
): EntryProperty<T> {
    return entry(
        scope = this,
        descriptor = definition()
    )
}