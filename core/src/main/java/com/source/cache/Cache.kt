package com.source.cache

import com.source.storage.Storage
import kotlin.reflect.KClass

/**
 * @project Bricks
 * @author SourceOne on 25.10.2019
 */

/**
 * Describes how to communicate with any cache
 * */
interface CacheStore<Id, Type> : Storage<Id, Type>

/**
 * Simple [CacheStore] implementation
 * */
fun <Id, Type> cache(storage: Storage<Id, Type>): CacheStore<Id, Type> {
    return object : CacheStore<Id, Type> {
        override fun get(index: Id): Type? {
            return storage[index]
        }

        override fun set(index: Id, value: Type) {
            storage[index] = value
        }
    }
}

/**
 * Wraps [source] with simple implementation of cache.
 * Caches only items of type successType
 * */
inline fun <Id, Type: Any, Success : Any> cache(
    source: CacheStore<Id, Type>,
    successType: KClass<in Success> = Any::class,
    crossinline provider: (Id) -> Type
): (Id) -> Type {
    return { input ->
        val cache = source[input]
        if (cache != null)
            cache
        else {
            val res = provider(input)
            if (successType == res::class) {
                println("$successType == ${res::class}")
                source[input] = res
            }
            res
        }
    }
}


/**
 * Wraps [source] with simple implementation of cache.
 * Caches every item provided by [provider]
 * */
inline fun <Id, Type: Any> cache(
    source: CacheStore<Id, Type>,
    crossinline provider: (Id) -> Type
): (Id) -> Type {
    return cache(source = source, successType= Any::class, provider = provider)
}