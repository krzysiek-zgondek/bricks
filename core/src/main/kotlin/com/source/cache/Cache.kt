package com.source.cache

import com.source.indexable.indexedvalue.IndexedValue
import com.source.indexable.indexedvalue.index
import com.source.provider.provider
import com.source.storage.Storage
import com.source.storage.map.hashMapStorage

/**
 * @project Bricks
 * @author SourceOne on 25.10.2019
 */

/**
 * Describes how to communicate with any cache
 * */
@Suppress("NOTHING_TO_INLINE")
abstract class CacheStore<Id, Type> : Storage<Id, Type> {
    inline operator fun IndexedValue<Id, Type>.unaryPlus(): Type {
        set(id, value)
        return value
    }
}

/**
 * Simple [CacheStore] implementation
 * */
inline fun <Id, Type> cache(
    storage: Storage<Id, Type> = hashMapStorage(),
    crossinline discriminator: (IndexedValue<Id, Type>) -> Boolean
): CacheStore<Id, Type> {
    return object : CacheStore<Id, Type>() {
        override fun get(index: Id): Type? {
            return storage[index]
        }

        override fun set(index: Id, value: Type) {
            if (!discriminator(value.index(index)))
                storage[index] = value
        }
    }
}

/**
 * Uses [cache] and wraps it with a provider implementation.
 * Uses [CacheStore.unaryPlus] in combination with [provider]
 * to store elements in the [cache]
 *
 * */
inline fun <Id, Type> provider(
    cache: CacheStore<Id, Type>,
    crossinline provider: (Id) -> Type
): (Id) -> Type {
    return provider(
        context = cache,
        init = { input: Id -> get(input) },
        next = { input, last ->
            last ?: +provider(input).index(input)
        }
    )
}