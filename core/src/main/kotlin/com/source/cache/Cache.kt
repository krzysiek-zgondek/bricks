package com.source.cache

import com.source.indexable.indexedvalue.IndexedValue
import com.source.indexable.indexedvalue.index
import com.source.provider.provide
import com.source.bricks.storage.Storage
import com.source.bricks.storage.map.hashMapStorage
import kotlinx.coroutines.CoroutineScope

/**
 * @project Bricks
 * @author SourceOne on 25.10.2019
 */

/**
 * Describes how to communicate with any cache
 * */
open class CacheStore<Id, Type>(
    @PublishedApi
    internal val storage: Storage<Id, Type?>
) {

    open operator fun get(id: Id): Type? {
        return storage[id]
    }

    open operator fun set(id: Id, value: Type) {
        storage[id] = value
    }

    open fun remove(id: Id): Type? {
        return storage.remove(id)
    }


    @Suppress("NOTHING_TO_INLINE")
    inline operator fun IndexedValue<Id, Type>.unaryPlus(): Type {
        set(id, value)
        return value
    }

    @Suppress("NOTHING_TO_INLINE")
    inline operator fun plus(indexedValue: IndexedValue<Id, Type>): Type {
        set(indexedValue.id, indexedValue.value)
        return indexedValue.value
    }

    @Suppress("NOTHING_TO_INLINE")
    inline operator fun IndexedValue<Id, Type>.unaryMinus(): Type? {
        return remove(id)
    }

}

/**
 * Simple [CacheStore] implementation with which
 * rejects every object when [discriminator] returns true
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun <Id, Type> cache(
    storage: Storage<Id, Type?> = hashMapStorage<Id, Type>(),
    crossinline discriminator: (IndexedValue<Id, Type>) -> Boolean = { false }
): CacheStore<Id, Type> {
    return object : CacheStore<Id, Type>(storage) {
        override operator fun set(id: Id, value: Type) {
            if (!discriminator(value.index(id)))
                super.set(id, value)
        }
    }
}