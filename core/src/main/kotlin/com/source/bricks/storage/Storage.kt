package com.source.bricks.storage

/**
 * Storage interface
 * */
interface Storage<Id, Type> {
    val entries: List<Id>

    operator fun get(index: Id): Type
    operator fun set(index: Id, value: Type)

    fun remove(id: Id): Type

    fun clear()
}