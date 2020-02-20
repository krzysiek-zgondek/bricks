package com.source.bricks.entry.archive

import com.source.bricks.entry.archive.EntryArchive.Companion.archiveOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class EntryArchiveTest {
    class PersonV1
    class PersonV2
    class Person

    private val expectedResults = listOf(
        Pair(0L, PersonV1::class),
        Pair(1L, PersonV2::class)
    )

    lateinit var archive: EntryArchive<Person>

    @Before
    fun setup() {
        archive = archiveOf {
            entry { old: PersonV1 -> PersonV2() }
            entry(version = 5) { old: PersonV2 -> Person() }
        }
    }

    @Test
    fun `archive provides correct recovery paths`() {
        expectedResults.forEach { (version, expectedClass) ->
            val path = archive.findRecoveryPath(version)!!

            assertEquals(expectedClass, path.inputCls.kClass)
            assertEquals(Person::class, path.outputCls.kClass)
        }
    }

    @Test
    fun `archive provides correct version`() {
        assertEquals(5, archive.version)
    }
}