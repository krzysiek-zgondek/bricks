package com.source.bricks.storage.list

import com.source.bricks.storage.Storage

/**
 * Class which exposes every type of [MutableList] as [Storage]
 * */
class ListStorage<Type>(
    private val storage: MutableList<Type>
) : Storage<Int, Type> {
    override val entries: List<Int>
        get() = storage.indices.toList()

    override fun get(index: Int): Type {
        return storage[index]
    }

    override fun set(index: Int, value: Type) {
        storage[index] = value
    }

    override fun remove(id: Int): Type {
        return storage.removeAt(id)
    }

    override fun clear() {
        storage.clear()
    }
}


/**
 * Creates [ListStorage] object using provided list [MutableList]
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun <Type> listStorage(list: MutableList<Type>): ListStorage<Type> {
    return ListStorage(list)
}

/**
 * Creates [ListStorage] object using [MutableList] implementation
 * You can initialize map using [element] argument
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun <Type> listStorage(vararg element: Type): ListStorage<Type> {
    return listStorage(
        mutableListOf(*element)
    )
}