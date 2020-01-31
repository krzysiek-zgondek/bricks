package com.source.bricks.provider

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


/**
 * Wraps [provideDelegate] operator so it can be used with inline reified functions
 * */
interface DelegateProvider<Ref, Type> {
    operator fun provideDelegate(
        ref: Any, prop: KProperty<*>
    ): ReadOnlyProperty<Ref, Type>
}

/**
 * Creates simple implementation of [DelegateProvider] object with [provider]
 * */
inline fun <Ref, reified Type>
        readOnlyProperty(crossinline provider: (Ref, KProperty<*>) -> Type): ReadOnlyProperty<Ref, Type> {
    return object : ReadOnlyProperty<Ref, Type> {
        override fun getValue(thisRef: Ref, property: KProperty<*>): Type {
            return provider(thisRef, property)
        }
    }
}