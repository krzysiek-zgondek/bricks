package com.source.bricks.entry.scope.group

import com.source.bricks.entry.scope.EntryScope
import com.source.bricks.test.any
import com.source.bricks.test.mock
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.times

/**
 * @author SourceOne on 20.02.2020
 * @project Bricks
 */
class CollectionScopeGroupTest {
    @Test
    fun `adding to scope`() {
        val mockedList = mock<MutableList<EntryScope>>()
        val group = listedScopeGroup(list = mockedList)

        group.add(mock())
        Mockito.verify(mockedList).add(any())

        group.register { mock<EntryScope>() }
        Mockito.verify(mockedList, times(2)).add(any())
    }

    @Test
    fun `removing from scope`() {
        val mockedList = mock<MutableList<EntryScope>>()
        val group = listedScopeGroup(list = mockedList)

        group.remove(mock())
        Mockito.verify(mockedList).remove(any())
    }

    @Test
    fun `clearAllScopes should clear all contained scopes`() {
        val expectedValue = 5
        val list = mutableListOf<EntryScope>()

        val scope = listedScopeGroup(list = list)
        repeat(expectedValue) {
            scope.add(mock())
        }

        scope.clearAllScopes()

        list.onEach {
            Mockito.verify(it).clear()
        }
    }
}