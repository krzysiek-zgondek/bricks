package com.source.bricks.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import com.source.bricks.activity.argument.argument
import com.source.bricks.activity.argument.arguments
import com.source.bricks.activity.intent.intentFor
import common.addTestActivities
import kotlinx.android.parcel.Parcelize
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.Serializable

/**
 * @author SourceOne on 16.01.2020
 * @project Bricks
 */

class DirectArgumentActivity : Activity() {
    data class Serial(val value: Int, val text: String) : Serializable
    @Parcelize
    data class Parcel(val value: Int, val text: String) : Parcelable

    var text by Arguments.TextParam
    val serial by Arguments.SerialParam
    val parcel by Arguments.ParcelParam
    val notProvided by Arguments.NotProvidedParam

    companion object {
        private object Arguments {
            val TextParam by argument(default = "")
            val SerialParam by argument<Serial>()
            val ParcelParam by argument<Parcel>()
            val NotProvidedParam by argument<Parcel>()
        }

        fun getIntent(context: Context): Intent {
            return context.intentFor<DirectArgumentActivity> {
                arguments {
                    Arguments.TextParam.set("test")
                    Arguments.SerialParam.set(Serial(1, "test"))
                    Arguments.ParcelParam.set(Parcel(1, "test"))
                }
            }
        }
    }
}

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ArgumentTest {
    lateinit var scenario: ActivityScenario<DirectArgumentActivity>

    @Before
    fun setUp() {
        addTestActivities(
            DirectArgumentActivity::class
        )

        val context = ApplicationProvider.getApplicationContext<Context>()
        scenario = launchActivity(DirectArgumentActivity.getIntent(context))
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun `provide arguments and check if they were obtained properly`() {
        scenario.onActivity { activity ->
            Assert.assertEquals("test", activity.text)
            Assert.assertNotEquals("test2", activity.text)
            Assert.assertEquals(DirectArgumentActivity.Serial(1, "test"), activity.serial)
            Assert.assertEquals(DirectArgumentActivity.Parcel(1, "test"), activity.parcel)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `reading nonNull arguments which were not provided should throw`() {
        scenario.onActivity { activity ->
            val provided = activity.notProvided
            provided.text.hashCode()
        }
    }

    @Test
    fun `change argument and check if value was properly stored`() {
        scenario.onActivity { activity ->
            activity.text = "test 2"
            Assert.assertEquals("value should be changed right away", "test 2", activity.text)
        }

        scenario.recreate()
        scenario.onActivity { activity ->
            Assert.assertEquals(
                "value should still be changed after recreation",
                "test 2",
                activity.text
            )
        }
    }
}