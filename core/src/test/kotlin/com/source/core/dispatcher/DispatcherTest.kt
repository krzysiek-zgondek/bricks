package com.source.core.dispatcher

import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

/**
 * @author SourceOne on 15.01.2020
 * @project Bricks
 */
class DispatcherTest {
    @Test
    fun `test simple dispatcher`() {
        val receivers = mutableListOf("first", "second")

        val dispatcher = Dispatcher.Builder.build<String> {
            add { dispatch -> receivers[0] = dispatch }
            add { dispatch -> receivers[1] = dispatch }
        }

        val message = "test"

        dispatcher.dispatch(message)

        receivers.forEach { dispatch ->
            Assert.assertEquals(message, dispatch)
        }
    }

    @Test
    fun `test object dispatcher`() {
        data class Dispatch(val code: Int, val tag: String)

        val receivers = mutableListOf(
            Dispatch(0, "init"),
            Dispatch(0, "init")
        )

        val dispatcher = Dispatcher.Builder<Dispatch>().apply {
            add { dispatch -> receivers[0] = dispatch }
            add { dispatch -> receivers[1] = dispatch }
        }.build()

        val message = Dispatch(1, "proper dispatch")

        dispatcher.dispatch { message }

        receivers.forEach { dispatch ->
            Assert.assertEquals(message, dispatch)
        }
    }
}