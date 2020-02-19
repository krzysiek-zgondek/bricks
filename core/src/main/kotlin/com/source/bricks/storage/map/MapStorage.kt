package com.source.bricks.storage.map

import com.source.bricks.storage.Storage

/**
 * Class which exposes every type of [Map] as [Storage]
 * */
class MapStorage<Id, Type>(
    private val storage: MutableMap<Id, Type?>
) : Storage<Id, Type?> {
    override val entries: List<Id>
        get() = storage.keys.toList()

    override operator fun get(index: Id): Type? {
        return storage[index]
    }

    override operator fun set(index: Id, value: Type?) {
        storage[index] = value
    }

    override fun remove(id: Id): Type? {
        return storage.remove(id)
    }

    override fun clear() {
        storage.clear()
    }
}

/**
 * Creates [MapStorage] object using provided map [MutableMap]
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun <Id, Type> mapStorage(map: MutableMap<Id, Type?>): MapStorage<Id, Type> {
    return MapStorage(map)
}

/**
 * Creates [MapStorage] object using [HashMap] implementation
 * You can initialize map using [pair] argument
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun <Id, Type> hashMapStorage(vararg pair: Pair<Id, Type?>): MapStorage<Id, Type> {
    return mapStorage(
        hashMapOf(*pair)
    )
}