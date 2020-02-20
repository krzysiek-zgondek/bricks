package com.source.bricks.view

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Space
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import com.source.bricks.view.ViewGroupsTestActivity.Companion.layoutToFindId
import com.source.bricks.view.ViewGroupsTestActivity.Companion.viewToStopOnId
import com.source.bricks.test.addTestActivities
import com.source.bricks.test.any
import com.source.bricks.test.mock
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


class ViewGroupsTestActivity : Activity() {
    lateinit var layoutToFind: View
    lateinit var viewToStopOn: View

    val view by lazy {
        LinearLayout(this).apply {
            layoutToFind = LinearLayout(context).apply { id = layoutToFindId }
            addView(layoutToFind)

            viewToStopOn = Space(context).apply { id = viewToStopOnId }
            addView(viewToStopOn)

            repeat(2) {
                val root = LinearLayout(context).apply {
                    repeat(4) { addView(Space(context)) }
                }
                addView(root)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view)
    }

    companion object {
        val layoutToFindId = View.generateViewId()
        val viewToStopOnId = View.generateViewId()
    }
}

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ViewGroupsTest {
    open class Invoker : (View) -> Unit {
        override fun invoke(p1: View) = Unit
    }

    open class Matcher : (View) -> Boolean {
        override fun invoke(p1: View): Boolean = p1.id == viewToStopOnId
    }

    lateinit var invoker: Invoker
    lateinit var matcher: Matcher
    lateinit var scenario: ActivityScenario<ViewGroupsTestActivity>

    @Before
    fun setUp() {
        addTestActivities(
            ViewGroupsTestActivity::class
        )

        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.STARTED)

        invoker = mock()
        matcher = spy(Matcher())
    }

    @Test
    fun `forEachAllRecursive goes trough all provided views`() {
        scenario.onActivity { activity ->
            activity.view.forEachAllRecursive(invoker)
            verify(invoker, times(12)).invoke(any())
        }
    }

    @Test
    fun `forEachAll goes trough all provided views`() {
        scenario.onActivity { activity ->
            activity.view.forEachAll(invoker)
            verify(invoker, times(12)).invoke(any())
        }
    }

    @Test
    fun `findFirstRecursive goes trough all provided views and stops on first match`() {
        scenario.onActivity { activity ->
            activity.view.findFirstRecursive(matcher)
            verify(matcher, times(2)).invoke(any())
            verify(matcher).invoke(activity.viewToStopOn)
        }
    }

    @Test
    fun `findFirst goes trough all provided views and stops on first match`() {
        scenario.onActivity { activity ->
            activity.view.findFirst(matcher)
            verify(matcher, times(2)).invoke(any())
            verify(matcher).invoke(activity.viewToStopOn)
        }
    }

    @Test
    fun `onViewById invokes recevier on first found id`() {
        scenario.onActivity { activity ->
            activity.view.onViewById<ViewGroup>(layoutToFindId, invoker)
            verify(invoker, times(1)).invoke(activity.layoutToFind)
        }
    }

    @Test
    fun `onViewById does not invoke recevier if id was not found`() {
        scenario.onActivity { activity ->
            activity.view.onViewById<ViewGroup>(12345678, invoker)
            verify(invoker, times(0)).invoke(any())
        }
    }
}
