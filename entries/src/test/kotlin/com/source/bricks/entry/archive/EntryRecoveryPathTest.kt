package com.source.bricks.entry.archive

import org.junit.Assert.assertEquals
import org.junit.Test


class EntryRecoveryPathTest {
    class PersonV1
    data class PersonV2(val name: String)
    data class PersonV3(val id: String, val name: String)
    data class Person(val id: Long, val name: String)

    private val operations = listOf(
        entryUpdate(version = 1) { _: PersonV1 ->
            PersonV2("john")
        },
        entryUpdate(version = 2) { old: PersonV2 ->
            PersonV3("1234", old.name)
        },
        entryUpdate(version = 3) { old: PersonV3 ->
            Person(old.id.toLong(), "new ${old.name}")
        }
    )

    private val expected = Person(1234L, "new john")

    @Test
    fun `provides correct input and output classes`() {
        val path = EntryRecoveryPath<Person>(operations)
        assertEquals(PersonV1::class, path.inputCls.kClass)
        assertEquals(Person::class, path.outputCls.kClass)
    }

    @Test
    fun `provides correctly recovered object`() {
        val path = EntryRecoveryPath<Person>(operations)
        val old = PersonV1()
        val new = path.apply(old)
        assertEquals(expected, new)
    }
}