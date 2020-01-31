package com.source.bricks.fragment.manager.content

import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commitNow
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import com.source.bricks.fragment.manager.content.FragmentManagerTestActivity.Companion.container1
import com.source.bricks.test.addTestActivities
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
class SwitchResultsManipulatorsKtTest {
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

    @Test
    fun `whenAvailable execute when fragment becomes active`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            var wasCalled = false
            with(manager) {
                val result =
                    switchTo { TestFragment1() }
                whenRecycled(result) {
                    wasCalled = true
                }
            }
            Assert.assertEquals(false, wasCalled)

            manager.executePendingTransactions()

            wasCalled = false
            with(manager) {
                val result =
                    switchTo { TestFragment1() }
                whenRecycled(result) {
                    wasCalled = true
                }
            }
            Assert.assertEquals(true, wasCalled)
        }
    }
}