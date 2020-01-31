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
class FragmentDescriptorManipulatorsKtTest {
    class TestFragment1 : Fragment()

    private val descriptor =
        fragmentDescriptor<TestFragment1>(container = container1)

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
                whenAvailable(descriptor) {
                    wasCalled = true
                }
                commitFragment()
            }
            Assert.assertEquals(true, wasCalled)

            wasCalled = false
            with(manager) {
                commitFragment()

                whenAvailable(descriptor) {
                    wasCalled = true
                }
            }
            Assert.assertEquals(false, wasCalled)
        }
    }

    @Test
    fun `whenActiveClass execute when fragment is already active`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            var wasCalled = false
            with(manager) {
                whenActiveClass(descriptor) {
                    wasCalled = true
                }
                commitFragment()
                commitFragment()
            }
            Assert.assertEquals(false, wasCalled)

            wasCalled = false
            with(manager) {
                commitFragment()
                commitFragment()

                //this should not be called because fragment is already active
                whenActiveClass(descriptor) {
                    wasCalled = true
                    Assert.assertEquals(4, it.size)
                }
            }
            Assert.assertEquals(true, wasCalled)
        }
    }

    @Test
    fun `whenActiveTag execute when fragment is already active`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            var wasCalled = false
            with(manager) {
                whenActiveTag(descriptor) {
                    wasCalled = true
                }
                commitFragment()
                commitFragment()
            }
            Assert.assertEquals(false, wasCalled)

            wasCalled = false
            with(manager) {
                commitFragment()
                commitFragment()

                //this should not be called because fragment is already active
                whenActiveClass(descriptor) {
                    wasCalled = true
                    Assert.assertEquals(4, it.size)
                }
            }
            Assert.assertEquals(true, wasCalled)
        }
    }

    private fun FragmentManager.commitFragment() {
        commitNow {
            add(
                descriptor.container,
                descriptor.instanceCls.createInstance(),
                descriptor.tag
            )
        }
    }
}