package com.source.bricks.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import com.source.bricks.activity.dispatch.ResultDispatch
import com.source.bricks.activity.dispatch.buildResultDispatcher
import common.addTestActivities
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

/**
 * @author SourceOne on 15.01.2020
 * @project Bricks
 */

class ResultActivity : AppCompatActivity()

class ReceivingActivity : AppCompatActivity() {
    lateinit var dispatched: ResultDispatch

    private val dispatcher =
        buildResultDispatcher {
            add { dispatch -> dispatched = dispatch }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        dispatcher.dispatch {
            ResultDispatch(
                requestCode,
                resultCode,
                data
            )
        }
    }

    fun startResultActivity() {
        val intent = Intent(this, ResultActivity::class.java)
        startActivityForResult(intent, 10001)
    }
}

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ResultDispatcherTests {
    lateinit var scenario: ActivityScenario<ReceivingActivity>

    @Before
    fun `prepare test activities`() {
        addTestActivities(
            ResultActivity::class, ReceivingActivity::class
        )

        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.CREATED)
    }


    @Test
    fun `start activity for result and check if results were provided`() {
        val expected = ResultDispatch(
            10001,
            Activity.RESULT_OK,
            null
        )

        scenario.onActivity { activity ->
            activity.startResultActivity()

            val shadow = shadowOf(activity)
            val shadowIntent = shadow.nextStartedActivityForResult.intent
            shadow.receiveResult(shadowIntent, Activity.RESULT_OK, null)

            Assert.assertEquals(expected, activity.dispatched)
        }
    }
}