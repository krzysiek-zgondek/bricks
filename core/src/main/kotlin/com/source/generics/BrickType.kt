package com.source.generics

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @project Bricks
 * @author SourceOne on 20.12.2019
 */


/**
 * Holds generic type
 * */
open class BrickType<T> {
    private val type: Type?

    init {
        val superclass = javaClass.genericSuperclass
        val parametrized = superclass as? ParameterizedType
        type = parametrized?.let {
            it.actualTypeArguments[0]
        }
    }

    override fun equals(other: Any?): Boolean = when {
        this === other -> true
        other !is BrickType<*> -> false
        type != other.type -> false
        else -> true
    }

    override fun hashCode(): Int {
        return type?.hashCode() ?: 0
    }
}

/**
 * Creates wrapper for erased generic type [T] and holds it in runtime
 * */
inline fun <reified T> type(): BrickType<T> {
    return object : BrickType<T>() {}
}

/**
 * Creates wrapper for erased generic type [T] and holds it in runtime
 * */
inline fun <reified T> T.toType(): BrickType<T> {
    return type()
}