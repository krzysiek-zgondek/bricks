package com.source.bricks.entry.archive

import com.source.bricks.reflect.tClass
import org.junit.Assert.assertEquals
import org.junit.Test


class EntryChainTest {
    class PersonV1
    class PersonV2
    class PersonV3
    class PersonV4
    class PersonV5
    class Person


    @Test(expected = IllegalArgumentException::class)
    fun `chain builder fails if classes are out of order`() {
        val builder = EntryChain.Builder()
        builder.entry { old: PersonV2 -> PersonV3() }
        builder.entry { old: PersonV1 -> PersonV2() }
        builder.entry { old: PersonV3 -> PersonV4() }
        builder.entry { old: PersonV4 -> Person() }
        builder.build<Person>()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `chain builder fails if version is out of order`() {
        val builder = EntryChain.Builder()
        builder.entry { old: PersonV1 -> PersonV2() }
        builder.entry(version = 5) { old: PersonV2 -> PersonV3() }
        builder.entry(version = 4) { old: PersonV3 -> PersonV4() }
        builder.entry { old: PersonV4 -> Person() }
        builder.build<Person>()
    }

    @Test(expected = IllegalStateException::class)
    fun `chain builder fails if last element is not root type`() {
        val builder = EntryChain.Builder()
        builder.entry { old: PersonV1 -> PersonV2() }
        builder.entry { old: PersonV2 -> PersonV3() }
        builder.entry { old: PersonV3 -> PersonV4() }
        builder.build<Person>()
    }

    @Test
    fun `chain builder output version is 0 for empty chain`() {
        val chain = EntryChain.Builder().build<Person>()
        assertEquals(0, chain.outputVersion)
    }

    @Test
    fun `chain builder output version is correctly grown`() {
        val expected = 4L

        val chain = EntryChain.Builder().apply {
            assertEquals(1L, nextVersion)
            entry { old: PersonV1 -> PersonV2() }
            assertEquals(2L, nextVersion)
            entry(version = 3) { old: PersonV2 -> PersonV3() }
            assertEquals(4L, nextVersion)
            entry { old: PersonV3 -> Person() }
        }.build<Person>()

        assertEquals(expected, chain.outputVersion)
    }

    @Test
    fun `chain builder creates correct amount of operations`() {
        val chain = EntryChain.Builder().apply {
            entry { old: PersonV1 -> PersonV2() }
            entry(version = 3) { old: PersonV2 -> PersonV3() }
            entry { old: PersonV3 -> Person() }
        }.build<Person>()

        assertEquals(3, chain.operations.size)
    }

    @Test
    fun `chain provides correct recovery paths`() {
        val operations = listOf(
            EntryUpdate(1, PersonV1::class.tClass, PersonV2::class.tClass, { PersonV2() }),
            EntryUpdate(2, PersonV2::class.tClass, PersonV3::class.tClass, { PersonV3() }),
            EntryUpdate(3, PersonV3::class.tClass, Person::class.tClass, { Person() })
        )

        val expectedResults = listOf(
            Pair(0L, PersonV1::class),
            Pair(1L, PersonV2::class),
            Pair(2L, PersonV3::class)
        )

        val chain = EntryChain<Person>(operations)

        expectedResults.forEach { (version, expectedClass) ->
            val path = chain.getApplicableChain(version)!!

            assertEquals(expectedClass, path.inputCls.kClass)
            assertEquals(Person::class, path.outputCls.kClass)
        }
    }
}