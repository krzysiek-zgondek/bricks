package com.source.cache

import com.source.indexable.indexedvalue.index
import com.source.log.log
import com.source.provider.get
import com.source.provider.provide
import com.source.resource.Resource
import com.source.resource.resource
import org.junit.Assert.*
import org.junit.Test

import java.net.URL

/**
 * @author SourceOne on 06.11.2019
 * @project Bricks
 */
internal class CacheKtTest {


    @Test
    fun provider() {
        val wrongItem = Resource.result("test1")

        val testProvider = provide(
            context = cache<String, Resource<String>>(
                discriminator = { Discriminate.not<Resource.Data<*>>(it.value) }
            ),
            init = { input: String -> get(input) },
            next = { input, cache ->
                cache ?: +resource { input }.index(input)
            }
        )

        val newItem = testProvider["test1"]
        val cachedItem = testProvider["test1"]

        assertSame(newItem, cachedItem)
        assertNotSame(newItem, wrongItem)
        assertNotSame(cachedItem, wrongItem)
    }
}