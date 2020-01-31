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
class HideTransactionsTest {
    class TestFragment1 : Fragment()
    class TestFragment2 : Fragment()

    private val testTag = "testTag"
    private val testTag2 = "testTag2"

    private val baseFragments1: List<FragmentDescriptor<TestFragment1>> = listOf(
        fragmentDescriptor(container1, false, testTag),
        fragmentDescriptor(container2, true, testTag)
    )

    private val baseFragments2: List<FragmentDescriptor<TestFragment2>> = listOf(
        fragmentDescriptor(container1, true, testTag),
        fragmentDescriptor(container2, false, testTag2),
        fragmentDescriptor(container2, false, testTag)
    )

    private val containerSize1 =
        (baseFragments1 + baseFragments2).count { it.container == container1 }

    private val containerSize2 =
        (baseFragments1 + baseFragments2).count { it.container == container2 }


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
                (baseFragments1 + baseFragments2).forEach { descriptor ->
                    add(
                        descriptor.container,
                        descriptor.instanceCls.createInstance(),
                        descriptor.tag
                    )
                }
            }
        }
    }

    @Test
    fun `HideTransactions_None should not hide any fragments`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager
            baseFragments1.forEach { descriptor ->
                testTransactionProcedure(
                    manager,
                    HideTransactions::None,
                    descriptor,
                    0,
                    0,
                    0,
                    0
                )
            }

            baseFragments2.forEach { descriptor ->
                testTransactionProcedure(
                    manager,
                    HideTransactions::None,
                    descriptor,
                    0,
                    0,
                    0,
                    0
                )
            }
        }
    }

    @Test
    fun `HideTransactions_All should hide all fragments in fragmentManager`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            testTransactionProcedure(
                manager,
                HideTransactions::All,
                baseFragments1[0],
                baseFragments1.size,
                baseFragments2.size,
                containerSize1,
                containerSize2
            )
        }
    }

    @Test
    fun `HideTransactions_All should hide all fragments in container`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            testTransactionProcedure(
                manager,
                HideTransactions::All,
                baseFragments1[1],
                1,
                baseFragments2.size - 1,
                0,
                containerSize2
            )
        }
    }

    @Test
    fun `HideTransactions_Class should hide matching type fragments in fragmentManager`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            testTransactionProcedure(
                manager,
                HideTransactions::Class,
                baseFragments1[0],
                baseFragments1.size,
                0,
                1,
                1
            )
        }
    }

    @Test
    fun `HideTransactions_Class should hide matching type fragments in container`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            testTransactionProcedure(
                manager,
                HideTransactions::Class,
                baseFragments1[1],
                1,
                0,
                0,
                1
            )
        }
    }

    @Test
    fun `HideTransactions_Tag should hide matching tag fragments in fragmentManager`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            testTransactionProcedure(
                manager,
                HideTransactions::Tag,
                baseFragments1[0],
                baseFragments1.size,
                baseFragments2.size - 1,
                containerSize1,
                containerSize2 - 1
            )
        }
    }

    @Test
    fun `HideTransactions_Tag should hide matching tag fragments in container`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            testTransactionProcedure(
                manager,
                HideTransactions::Tag,
                baseFragments1[1],
                1,
                1,
                0,
                containerSize2 - 1
            )
        }
    }

    @Test
    fun `HideTransactions_ClassTag should hide matching class and tag fragments in fragmentManager`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            testTransactionProcedure(
                manager,
                HideTransactions::ClassTag,
                baseFragments1[0],
                baseFragments1.size,
                0,
                1,
                1
            )
        }
    }


    @Test
    fun `HideTransactions_ClassTag should hide matching class and tag fragments in container`() {
        scenario.onActivity { activity ->
            val manager = activity.supportFragmentManager

            testTransactionProcedure(
                manager,
                HideTransactions::ClassTag,
                baseFragments2[0],
                0,
                1,
                1,
                0
            )
        }
    }


    private inline fun <reified T : Fragment> testTransactionProcedure(
        manager: FragmentManager,
        transaction: HideTransaction<T>,
        descriptor: FragmentDescriptor<T>,
        expectedFragments1: Int,
        expectedFragments2: Int,
        expectedInContainer1: Int,
        expectedInContainer2: Int
    ) {
        manager.commitNow {
            transaction(manager, this, descriptor)
        }
        manager.executePendingTransactions()

        val hiddenFragments1 = manager.fragments.filter { it is TestFragment1 && it.isHidden }
        val hiddenFragments2 = manager.fragments.filter { it is TestFragment2 && it.isHidden }
        val hiddenContainer1 = manager.fragments.filter { it.id == container1 && it.isHidden }
        val hiddenContainer2 = manager.fragments.filter { it.id == container2 && it.isHidden }

        Assert.assertEquals(expectedFragments1, hiddenFragments1.size)
        Assert.assertEquals(expectedFragments2, hiddenFragments2.size)
        Assert.assertEquals(expectedInContainer1, hiddenContainer1.size)
        Assert.assertEquals(expectedInContainer2, hiddenContainer2.size)
    }
}