package com.source.bricks.view

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import common.addTestActivities
import common.any
import common.mock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


class ViewGroupsBuilderTestActivity : Activity()

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ViewGroupsBuildersTest {
    lateinit var scenario: ActivityScenario<ViewGroupsBuilderTestActivity>

    @Before
    fun setUp() {
        addTestActivities(
            ViewGroupsBuilderTestActivity::class
        )

        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun `add adds the view to parent hierarchy`() {
        scenario.onActivity { activity ->
            val view = LinearLayout(activity)

            view.add(width = matchParent, height = matchParent) { Space(context) }
            with(view.getChildAt(0)) {
                Assert.assertTrue(this is Space)
                Assert.assertEquals(matchParent, layoutParams.width)
                Assert.assertEquals(matchParent, layoutParams.height)
            }

            view.add { Space(context) }
            with(view.getChildAt(1)) {
                Assert.assertTrue(this is Space)
                Assert.assertEquals(wrapContent, layoutParams.width)
                Assert.assertEquals(wrapContent, layoutParams.height)
            }

            Assert.assertEquals(2, view.childCount)
        }
    }


    @Test
    fun `add does not override layoutParams width and height `() {
        scenario.onActivity { activity ->
            val view = LinearLayout(activity)

            val lp = ViewGroup.LayoutParams(matchParent, matchParent)
            view.add { Space(context).apply { layoutParams = lp } }

            with(view.getChildAt(0)) {
                Assert.assertEquals(matchParent, layoutParams.width)
                Assert.assertEquals(matchParent, layoutParams.height)
            }
        }
    }

    @Test
    fun `add overrides layoutParams width and height `() {
        scenario.onActivity { activity ->
            val view = LinearLayout(activity)

            val lp = ViewGroup.LayoutParams(matchParent, matchParent)
            view.add(override = true) { Space(context).apply { layoutParams = lp } }

            with(view.getChildAt(0)) {
                Assert.assertEquals(wrapContent, layoutParams.width)
                Assert.assertEquals(wrapContent, layoutParams.height)
            }
        }
    }

    @Test
    fun `include inflates and adds view to parent `() {
        scenario.onActivity { activity ->
            val view = LinearLayout(activity)

            view.include<View>(layoutId = android.R.layout.simple_list_item_1)
            with(view.getChildAt(0)) {
                Assert.assertTrue(this is TextView)
                Assert.assertEquals(matchParent, layoutParams.width)
                Assert.assertEquals(wrapContent, layoutParams.height)
            }
        }
    }
}