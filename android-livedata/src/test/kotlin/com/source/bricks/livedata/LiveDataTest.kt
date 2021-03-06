package com.source.bricks.livedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData
import com.source.bricks.test.mock
import com.source.bricks.test.mockCall1
import com.source.bricks.test.mockCall2
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.*

/**
 * @author SourceOne on 29.01.2020
 * @project Bricks
 */

class LiveDataTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var data: MutableLiveData<String>
    lateinit var registry: LifecycleRegistry

    @Before
    fun setUp() {
        data = MutableLiveData()
        registry = LifecycleRegistry(mock())
    }

    @Test
    fun `observeOnce - receiver should be called only once`() {
        val observer = mockCall1(data::observeOnce)

        data.postValue("test")
        verify(observer, times(1)).invoke("test")

        data.postValue("wrong test")
        verify(observer, times(1)).invoke("test")
    }

    @Test
    fun `observeOnce with owner - receiver should be called only once`() {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        val (_, observer)
                = mockCall2(data::observeOnce) { owner, _ ->
            `when`(owner.lifecycle).thenReturn(registry)
        }

        data.postValue("test")
        verify(observer, times(1)).invoke("test")

        data.postValue("wrong test")
        verify(observer, times(1)).invoke("test")
    }

    @Test
    fun `observeOnce with owner - receiver should be called with old value only once`() {
        val (_, observer)
                = mockCall2(data::observeOnce) { owner, _ ->
            `when`(owner.lifecycle).thenReturn(registry)
        }

        data.postValue("test")
        verify(observer, times(0)).invoke(any())

        registry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        verify(observer, times(1)).invoke("test")

        data.postValue("wrong test")
        verify(observer, times(1)).invoke("test")
    }
}