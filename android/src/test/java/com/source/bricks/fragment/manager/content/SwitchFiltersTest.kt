package com.source.bricks.fragment.manager.content

import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commitNow
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import com.source.bricks.fragment.manager.content.FragmentManagerTestActivity.Companion.container1
import com.source.bricks.fragment.manager.content.FragmentManagerTestActivity.Companion.container2
import common.addTestActivities
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.reflect.full.createInstance

/**
 * @author SourceOne on 24.01.2020
 * @project Bricks
 */

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class SwitchFiltersTest {
    class TestFragment1 : Fragment()
    class TestFragment2 : Fragment()

    lateinit var scenario: ActivityScenario<FragmentManagerTestActivity>

    @Before
    fun setUp() {
        addTestActivities(
            FragmentManagerTestActivity::class
        )

        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.STARTED)
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager
            manager.commitNow {
                add(container1, TestFragment1(), "TestFragment1")
                add(container1, TestFragment2(), "TestFragment2")
                add(container2, TestFragment1(), "TestFragment1")
                add(container2, TestFragment2(), "TestFragment2")
                add(container2, TestFragment2(), "TestFragment2")
            }
        }
    }

    @Test
    fun `SwitchFilter filterContainer should return all fragments when containerOnly is false`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            val result =
                SwitchFilters.filterContainer<TestFragment1>(
                    manager, fragmentDescriptor(container1, false, null)
                )

            Assert.assertEquals(5, result.size)
        }
    }

    @Test
    fun `SwitchFilter filterContainer should return only fragments from container - containerOnly`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            val result =
                SwitchFilters.filterContainer<TestFragment1>(
                    manager, fragmentDescriptor(container1, true, null)
                )

            Assert.assertEquals(2, result.size)
        }
    }

    @Test
    fun `SwitchFilter filterClass should return only fragments of same type`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            val result =
                SwitchFilters.filterClass<TestFragment1>(
                    manager, fragmentDescriptor(container1, false, null)
                )

            Assert.assertEquals(2, result.size)
        }
    }

    @Test
    fun `SwitchFilter filterClass should return only fragments of same type - containerOnly`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            val result =
                SwitchFilters.filterClass<TestFragment1>(
                    manager, fragmentDescriptor(container1, true, null)
                )

            Assert.assertEquals(1, result.size)
        }
    }

    @Test
    fun `SwitchFilter filterTag should return only fragments of same tag`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            val result =
                SwitchFilters.filterTag<TestFragment2>(
                    manager, fragmentDescriptor(container2, false, "TestFragment2")
                )

            Assert.assertEquals(3, result.size)
        }
    }

    @Test
    fun `SwitchFilter filterTag should return only fragments of same tag - containerOnly`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            val result =
                SwitchFilters.filterTag<TestFragment2>(
                    manager, fragmentDescriptor(container2, true, "TestFragment2")
                )

            Assert.assertEquals(2, result.size)
        }
    }

    @Test
    fun `SwitchFilter filterClassTag should return only fragments of same tag and type`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            val result =
                SwitchFilters.filterClassTag<TestFragment2>(
                    manager, fragmentDescriptor(container2, false, "TestFragment2")
                )

            Assert.assertEquals(3, result.size)
        }
    }

    @Test
    fun `SwitchFilter filterClassTag should return only fragments of same tag and type - containerOnly`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            val result =
                SwitchFilters.filterClassTag<TestFragment2>(
                    manager, fragmentDescriptor(container2, true, "TestFragment2")
                )

            Assert.assertEquals(2, result.size)
        }
    }
}