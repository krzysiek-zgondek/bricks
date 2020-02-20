package com.source.bricks.entry

import com.source.bricks.entry.scope.EntryScope
import com.source.bricks.entry.scope.scope
import com.source.bricks.entry.transcoder.EntryTranscoder
import com.source.bricks.storage.Storage
import com.source.bricks.test.any
import com.source.bricks.test.mock
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

/**
 * @author SourceOne on 20.02.2020
 * @project Bricks
 */


class EntryFactoryTest {
    open class _EntryScope(scope: EntryScope) : EntryScope by scope

    class Person

    lateinit var transcoder: EntryTranscoder<Any>
    lateinit var storage: Storage<EntryKey, Entry<Any>?>
    lateinit var scope: _EntryScope

    @Before
    fun setup() {
        transcoder = mock()
        storage = mock()
        scope = Mockito.spy(
            _EntryScope(scope(transcoder = transcoder, storage = storage))
        )
    }

    @Test(expected = ClassCastException::class)
    fun `entry property get`() {
        val factory = mock<() -> Person>()
        Mockito.`when`(factory.invoke()).thenAnswer { Person() }

        val prop by entry<Person>(scope, factory = factory)
        val value = prop

        Mockito.verify(scope).get<Person>(any(), any())
        Mockito.verify(factory).invoke()

        val prop2 by entry<Person>(scope, factory = mock())
        val value2 = prop2

    }

    @Test(expected = ClassCastException::class)
    fun `entry property get - define`() {
        val factory = mock<() -> Person>()
        Mockito.`when`(factory.invoke()).thenAnswer { Person() }

        val prop by entry<Person>(scope, define(factory = factory))
        val value = prop

        Mockito.verify(scope).get<Person>(any(), any())
        Mockito.verify(factory).invoke()

        val prop2 by entry<Person>(scope, define(factory = mock()))
        val value2 = prop2
    }


    @Test
    fun `entry property set`() {
        var prop by entry<Person>(scope)
        prop = Person()

        Mockito.verify(scope).set<Person>(any(), any(), any())
    }

    @Test
    fun `entry property set - define`() {
        var prop by entry<Person>(scope, define())
        prop = Person()

        Mockito.verify(scope).set<Person>(any(), any(), any())
    }
}