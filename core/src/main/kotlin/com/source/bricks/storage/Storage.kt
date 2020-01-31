package com.source.bricks.storage

/**
 * Storage interface
 * */
interface Storage<Id, Type> {
    operator fun get(index: Id): Type
    operator fun set(index: Id, value: Type)
    fun remove(id: Id): Type
}