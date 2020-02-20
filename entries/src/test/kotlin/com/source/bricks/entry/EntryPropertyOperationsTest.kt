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




class WithScope(scope: EntryScope){
    var person by entry<EntryPropertyOperationsTest.Person>(scope)

    fun remove(){
        person = EntryPropertyOperationsTest.Person()
        ::person.remove()
    }
}


class EntryPropertyOperationsTest{
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

    @Test
    fun `entry property remove`() {
        WithScope(scope).remove()
        Mockito.verify(scope).remove<Person>(any())
    }
}