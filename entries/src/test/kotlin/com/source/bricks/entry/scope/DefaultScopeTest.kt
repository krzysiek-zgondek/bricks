package com.source.bricks.entry.scope

import com.source.bricks.entry.Entry
import com.source.bricks.entry.EntryKey
import com.source.bricks.entry.archive.EntryArchive
import com.source.bricks.entry.define
import com.source.bricks.entry.transcoder.EntryTranscoder
import com.source.bricks.reflect.tClass
import com.source.bricks.storage.Storage
import com.source.bricks.test.any
import com.source.bricks.test.mock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito


class DefaultScopeTest {
    data class PersonV1(val name: String = "noname")
    data class Person(val name: String = "noname")

    private val olderDescriptor = define(
        id = EntryKey.defaultId<Person>(),
        factory = { PersonV1() }
    )

    private val newerDescriptor = define(
        factory = { Person() },
        archive = EntryArchive.archiveOf {
            entry { _: PersonV1 -> Person() }
        }
    )

    private val expectedOlderEntry = Entry<Any>(version = 0, data = PersonV1())
    private val expectedNewerEntry = Entry<Any>(version = 1, data = Person())

    lateinit var transcoder: EntryTranscoder<Any>
    lateinit var storage: Storage<EntryKey, Entry<Any>?>
    lateinit var scope: EntryScope

    @Before
    fun setup() {
        transcoder = mock()
        storage = mock()
        scope = scope(transcoder = transcoder, storage = storage)
    }

    @Test
    fun `getting entries key list`() {
        val list = scope.entries
        Mockito.verify(storage).entries
    }

    @Test
    fun `getting entry should use transcoder and storage`() {
        Mockito.`when`(storage[any()]).thenReturn(expectedOlderEntry)
        Mockito.`when`(transcoder.decode<Entry<PersonV1>>(any(), any()))
            .thenAnswer { it.arguments[0] }

        val entry = scope.get(olderDescriptor, PersonV1::class.tClass)
        assertEquals(expectedOlderEntry.data, entry)

        Mockito.verify(transcoder).decode<PersonV1>(any(), any())
        Mockito.verify(storage).get(any())
    }

    @Test
    fun `getting entry of older version stored should try recover it`() {
        Mockito.`when`(storage[any()]).thenReturn(expectedOlderEntry)
        Mockito.`when`(transcoder.decode<Entry<Any>>(any(), any()))
            .thenAnswer { it.arguments[0] }

        val entry = scope.get(newerDescriptor, Person::class.tClass)
        assertEquals(expectedNewerEntry.data, entry)

        Mockito.verify(transcoder).decode<PersonV1>(any(), any())
        Mockito.verify(storage).get(any())
    }

    @Test
    fun `setting entry should use transcoder and storage`() {
        Mockito.`when`(transcoder.encode<Entry<PersonV1>>(any(), any()))
            .thenReturn(expectedOlderEntry)

        val entry = scope.set(olderDescriptor, PersonV1(), PersonV1::class.tClass)
        Mockito.verify(transcoder).encode<Person>(any(), any())
        Mockito.verify(storage).set(any(), any())
    }

    @Test
    fun `removing entry should remove it from storage`() {
        scope.remove(olderDescriptor)
        Mockito.verify(storage).remove(any())
    }

    @Test
    fun `clearing scope should clear storage`() {
        scope.clear()
        Mockito.verify(storage).clear()
    }
}