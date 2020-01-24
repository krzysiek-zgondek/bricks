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

/**
 * @author SourceOne on 24.01.2020
 * @project Bricks
 */

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class SwitchTransactionsTest {
    class TestFragment1 : Fragment()
    class TestFragment2 : Fragment()

    private val base: FragmentDescriptor<TestFragment1> =
        fragmentDescriptor(container1, false, null)

    private val sameContainerOnly: FragmentDescriptor<TestFragment1> =
        base.copy(containerOnly = true)

    private val differentContainer: FragmentDescriptor<TestFragment1> =
        base.copy(container2, containerOnly = true)

    private val differentContainerTag: FragmentDescriptor<TestFragment1> =
        differentContainer.copy(tag = "other")


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
    fun `SwitchTransactions_Add should always allow to add fragment`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager
            commitTransaction(manager, SwitchTransactions::Add, base)
            Assert.assertEquals(1, manager.fragments.size)

            commitTransaction(manager, SwitchTransactions::Add, sameContainerOnly)
            Assert.assertEquals(2, manager.fragments.size)

            commitTransaction(manager, SwitchTransactions::Add, differentContainer)
            Assert.assertEquals(3, manager.fragments.size)

            commitTransaction(manager, SwitchTransactions::Add, differentContainer)
            Assert.assertEquals(4, manager.fragments.size)

            commitTransaction(manager, SwitchTransactions::Add, differentContainerTag)
            Assert.assertEquals(5, manager.fragments.size)
        }
    }

    @Test
    fun `SwitchTransactions_Replace should remove current fragment if exists and allow to spawn new one`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            commitTransaction(manager, SwitchTransactions::Replace, base)
            Assert.assertEquals(1, manager.fragments.size)

            commitTransaction(manager, SwitchTransactions::Replace, sameContainerOnly)
            Assert.assertEquals(1, manager.fragments.size)

            commitTransaction(manager, SwitchTransactions::Replace, differentContainer)
            Assert.assertEquals(2, manager.fragments.size)

            commitTransaction(manager, SwitchTransactions::Replace, differentContainer)
            Assert.assertEquals(2, manager.fragments.size)

            commitTransaction(manager, SwitchTransactions::Replace, differentContainerTag)
            Assert.assertEquals(3, manager.fragments.size)
        }
    }

    @Test
    fun `SwitchTransactions_Recycle should not allow to spawn new fragment if same already exists`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            var result = commitTransaction(manager, SwitchTransactions::Recycle, base)
            Assert.assertEquals(result, true)
            Assert.assertEquals(1, manager.fragments.size)

            result = commitTransaction(manager, SwitchTransactions::Recycle, base)
            Assert.assertEquals(result, false)
            Assert.assertEquals(1, manager.fragments.size)
        }
    }

    @Test
    fun `SwitchTransactions_Recycle should show already existing fragment if was hidden`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            var result = commitTransaction(manager, SwitchTransactions::Recycle, base)
            Assert.assertEquals(result, true)
            Assert.assertEquals(1, manager.fragments.size)

            manager.commitNow {
                hide(manager.fragments[0])
            }

            result = commitTransaction(manager, SwitchTransactions::Recycle, base)
            Assert.assertEquals(false, result)
            Assert.assertEquals(false, manager.fragments[0].isHidden)
            Assert.assertEquals(1, manager.fragments.size)
        }
    }


    private inline fun <reified T : Fragment> commitTransaction(
        manager: FragmentManager,
        transaction: SwitchTransaction<T>,
        descriptor: FragmentDescriptor<T>
    ): Boolean {
        var result = false

        manager.commitNow {
            result = transaction(manager, this, descriptor)
            if (transaction(manager, this, descriptor)) {
                add(descriptor.container, TestFragment1(), descriptor.tag)
            }
        }

        return result
    }
}