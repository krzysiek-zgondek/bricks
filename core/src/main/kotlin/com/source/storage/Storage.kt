package com.source.storage

/**
 * Storage description
 * */
interface Storage<Id, Type> {
    operator fun get(index: Id): Type?
    operator fun set(index: Id, value: Type)
}