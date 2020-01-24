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
class SwitchTest {
    class TestFragment1 : Fragment()

    lateinit var scenario: ActivityScenario<FragmentManagerTestActivity>

    @Before
    fun setUp() {
        addTestActivities(
            FragmentManagerTestActivity::class
        )

        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    /**
     * Note: current implementation of switch to is just a collection of other transactions
     * so test for it is very simple, just look if those transactions have been executed
     * */
    @Test
    fun `switchTo transaction execution`() {
        val expectedResult = SwitchResult<TestFragment1>(
            descriptor = fragmentDescriptor(),
            newInstance = true
        )

        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            var hideExecution = false
            var switchExecution = false
            var backstackExecution = false

            val result = manager.switchTo(
                hideTransaction = { _, _, _ -> hideExecution = true },
                switchTransaction = { _, _, _ -> switchExecution = true; true },
                backStackTransaction = { _, _, _, _ -> backstackExecution = true }
            ) {
                TestFragment1()
            }

            Assert.assertEquals(true, hideExecution)
            Assert.assertEquals(true, switchExecution)
            Assert.assertEquals(true, backstackExecution)
            Assert.assertEquals(expectedResult, result)
        }
    }
}