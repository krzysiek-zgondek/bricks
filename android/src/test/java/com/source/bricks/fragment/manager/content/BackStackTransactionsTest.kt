package com.source.bricks.fragment.manager.content

import android.os.Build
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import common.addTestActivities
import common.forceFieldValue
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class BackStackTransactionsTest {
    class TestFragment : Fragment()

    lateinit var scenario: ActivityScenario<FragmentManagerTestActivity>

    @Before
    fun setUp() {
        addTestActivities(
            FragmentManagerTestActivity::class
        )

        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.STARTED)
    }

//    @Test
//    fun `filterContainer should return all fragments`() {
//        val actual = listOf(
//            TestFragment1() to FragmentManagerTestActivity.container1,
//            TestFragment2() to FragmentManagerTestActivity.container1,
//
//            TestFragment1() to FragmentManagerTestActivity.container2,
//            TestFragment2() to FragmentManagerTestActivity.container2
//        )
//
//        scenario.onActivity { activity ->
//            activity.supportFragmentManager.commit {
//                actual.forEach { (fragment, container) ->
//                    add(container, fragment)
//                }
//            }
//
//
//        }
//    }

    @Test
    fun `BackStackTransactions_None should not add to backstack`() {
        scenario.onActivity { activity ->
            testTransactionProcedure(
                activity.supportFragmentManager,
                BackStackTransactions::None,
                0,
                null
            )
        }
    }

    @Test
    fun `BackStackTransactions_Always should always add to backstack with name equal to tag`() {
        scenario.onActivity { activity ->
            testTransactionProcedure(
                activity.supportFragmentManager,
                BackStackTransactions::Always,
                5,
                null
            )
        }
    }

    @Test
    fun `BackStackTransactions_AlwaysClass should always add to backstack with name equal to class`() {
        scenario.onActivity { activity ->
            testTransactionProcedure(
                activity.supportFragmentManager,
                BackStackTransactions::AlwaysClass,
                5,
                TestFragment::class.java.name
            )
        }
    }

    @Test
    fun `BackStackTransactions_New should add to backstack only new instances with name equal to tag`() {
        scenario.onActivity { activity ->
            testTransactionProcedure(
                activity.supportFragmentManager,
                BackStackTransactions::New,
                3,
                null
            )
        }
    }

    @Test
    fun `BackStackTransactions_New should add to backstack only new instances with name equal to class`() {
        scenario.onActivity { activity ->
            testTransactionProcedure(
                activity.supportFragmentManager,
                BackStackTransactions::NewClass,
                3,
                TestFragment::class.java.name
            )
        }
    }


    private fun testTransactionProcedure(
        manager: FragmentManager,
        transaction: BackStackTransaction<TestFragment>,
        expectedEntries: Int = 0,
        expectedName: String? = null
    ) {
        val descriptor = FragmentDescriptor(
            container = View.NO_ID,
            containerOnly = false,
            instanceCls = TestFragment::class
        )

        forceFieldValue(descriptor, "tag", null)
        forceFieldValue(descriptor, "instanceCls", null)
        manager.commit { transaction(manager, this, descriptor, false) }
        manager.commit { transaction(manager, this, descriptor, true) }

        forceFieldValue(descriptor, "containerOnly", true)
        manager.commit { transaction(manager, this, descriptor, false) }
        manager.commit { transaction(manager, this, descriptor, true) }
        manager.commit { transaction(manager, this, descriptor, true) }

        Assert.assertEquals(manager.backStackEntryCount, expectedEntries)
        repeat(manager.backStackEntryCount) { index ->
            val entry = manager.getBackStackEntryAt(index)
            Assert.assertEquals(expectedName, entry.name)
        }
    }
}