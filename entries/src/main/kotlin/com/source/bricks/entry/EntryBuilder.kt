package com.source.bricks.entry

import com.source.bricks.entry.scope.EntryScope
import com.source.bricks.reflect.tClass
import kotlin.reflect.KProperty


inline fun <reified T> entry(
        scope: EntryScope,
        crossinline factory: () -> T = ::kotlinInstance
): EntryProperty<T> {
    return entry(
            scope = scope,
            descriptor = define { factory() }
    )
}

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

inline fun <reified T> entry(
        scope: EntryScope,
        descriptor: EntryDescriptor<out T>
): EntryProperty<T> {
    return if (null is T) {
        buildEntry(
                scope = scope,
                descriptor = descriptor,
                default = { null as T }
        )
    } else {
        buildEntry(
                scope = scope,
                descriptor = descriptor,
                default = {
                    throw ClassCastException("Found null entry for NonNull type")
                }
        )
    }
}

@PublishedApi
internal inline fun <reified T> buildEntry(
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


inline fun <reified T> EntryScope.register(): EntryProperty<T> {
    return register() { define<T>() }
}

inline fun <reified T> EntryScope.register(
        definition: () -> EntryDescriptor<out T>
): EntryProperty<T> {
    return entry(
            scope = this,
            descriptor = definition()
    )
}
