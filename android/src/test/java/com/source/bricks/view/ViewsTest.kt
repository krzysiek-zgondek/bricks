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
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

class ViewsTestActivity : Activity() {
    lateinit var views: List<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this)
        setContentView(root)

        views = List(10) {
            Space(root.context)
        }.also { list ->
            list.forEach { view -> root.addView(view) }
        }
    }
}

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ViewsTest {
    lateinit var scenario: ActivityScenario<ViewsTestActivity>

    @Before
    fun setUp() {
        addTestActivities(
            ViewsTestActivity::class
        )

        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun `visible changes view state correctly`() {
        scenario.onActivity { activity ->
            activity.views[0].visible = false
            assertEquals(View.GONE, activity.views[0].visibility)

            activity.views[0].visible = true
            assertEquals(View.VISIBLE, activity.views[0].visibility)
        }
    }

    @Test
    fun `visible reads view state correctly`() {
        scenario.onActivity { activity ->
            activity.views[0].visible = false
            assertEquals(false, activity.views[0].visible)

            activity.views[0].visible = true
            assertEquals(true, activity.views[0].visible)
        }
    }


    @Test
    fun `visible changes state of views correctly`() {
        scenario.onActivity { activity ->
            visible(*activity.views.toTypedArray()) { false }
            activity.views.forEach { view ->
                assertEquals(false, view.visible)
            }

            visible(*activity.views.toTypedArray()) { true }
            activity.views.forEach { view ->
                assertEquals(true, view.visible)
            }
        }
    }
}