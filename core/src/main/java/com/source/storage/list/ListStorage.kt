package com.source.storage.list

import com.source.storage.Storage


/**
 * Class which exposes every type of [Map] as [Storage]
 * */
class ListStorage<Type> : Storage<Int, Type> {
    private val storage = mutableListOf<Type>()

    override fun get(index: Int): Type? {
        return storage[index]
    }

    override fun set(index: Int, value: Type) {
        storage[index] = value
    }
}