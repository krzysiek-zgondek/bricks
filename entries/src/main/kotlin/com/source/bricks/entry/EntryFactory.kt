package com.source.bricks.entry

import com.source.bricks.entry.scope.EntryScope
import com.source.bricks.reflect.tClass
import kotlin.reflect.KProperty


/**
 * Provides [EntryProperty] delegation for entry stored in [scope]. If [T] is not nullable
 * instance will be created using [factory]. Default is [kotlinInstance].
 *
 * @see kotlinInstance
 * @see javaInstance
 * */
inline fun <reified T> entry(
    scope: EntryScope,
    crossinline factory: () -> T = ::kotlinInstance
): EntryProperty<T> {
    return entry(
        scope = scope,
        descriptor = define { factory() }
    )
}

/**
 * Provides [EntryProperty] delegation for entry stored in [scope] using [id]. If [T] is not nullable
 * instance will be created using [factory]. Default is [kotlinInstance].
 *
 * @param any object that is used as a key to identify entries in scope's storage
 *
 * @see kotlinInstance
 * @see javaInstance
 * */
inline fun <reified T> entry(
    scope: EntryScope,
    id: Any,
    crossinline factory: () -> T = ::kotlinInstance
): EntryProperty<T> {
    return entry(
        scope = scope,
        descriptor = define(id = id) { factory() }
    )
}

/**
 * Provides [EntryProperty] delegation for entry stored in [scope] using [id]. If [T] is not nullable
 * instance will be created using [factory]. Default is [kotlinInstance].
 *
 * @param descriptor fully described entry and it's history
 *
 * @see EntryDescriptor
 * @see kotlinInstance
 * @see javaInstance
 * */
inline fun <reified T> entry(
    scope: EntryScope,
    descriptor: EntryDescriptor<out T>
): EntryProperty<T> {
    return if (null is T) {
        createEntry(
            scope = scope,
            descriptor = descriptor,
            default = { null as T }
        )
    } else {
        createEntry(
            scope = scope,
            descriptor = descriptor,
            default = {
                throw ClassCastException("Found null entry for NonNull type")
            }
        )
    }
}

/**
 * Provides [EntryProperty] delegation for entry stored in [scope] using [id]. If [T] is not nullable
 * instance will be created using [factory]. Default is [kotlinInstance].
 *
 * @param descriptor fully described entry and it's history
 *
 * @see EntryDescriptor
 * @see kotlinInstance
 * @see javaInstance
 * */
@PublishedApi
internal inline fun <reified T> createEntry(
    scope: EntryScope,
    descriptor: EntryDescriptor<out T>,
    crossinline default: () -> T
): EntryProperty<T> {
    val cls = T::class.tClass

    return object : EntryProperty<T> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return scope.get(descriptor, cls)
                ?: descriptor.factory()
                ?: return default()
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            scope.set(descriptor, value, cls)
        }

        override fun remove() {
            scope.remove(descriptor)
        }
    }
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