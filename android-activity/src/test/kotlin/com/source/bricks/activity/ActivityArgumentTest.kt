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
import com.source.bricks.activity.argument.set
import com.source.bricks.intent.intentFor
import com.source.bricks.test.addTestActivities
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

/*todo add null checking*/
class DirectArgumentActivity : Activity() {
    data class Serial(val value: Int, val text: String) : Serializable
    @Parcelize
    data class Parcel(val value: Int, val text: String) : Parcelable

    var text by Arguments.TextParam
    var text2 by Arguments.TextParam2
    val serial by Arguments.SerialParam
    val serial2 by Arguments.SerialParam2
    val parcel by Arguments.ParcelParam
    val parcel2 by Arguments.ParcelParam2
    val notProvidedDefault by Arguments.NotProvidedDefaultParam
    val notProvided by Arguments.NotProvidedParam

    companion object {
        private object Arguments {
            val TextParam by argument(default = "")
            val TextParam2 by argument(default = "")
            val SerialParam by argument<Serial>()
            val SerialParam2 by argument<Serial>()
            val ParcelParam by argument<Parcel>()
            val ParcelParam2 by argument<Parcel>()
            val NotProvidedDefaultParam by argument(default = "default")
            val NotProvidedParam by argument<Parcel>()
        }

        fun getIntent(context: Context): Intent {
            return context.intentFor<DirectArgumentActivity> {
                set(Arguments.TextParam, "test")
                set(Arguments.SerialParam, Serial(1, "test"))
                set(Arguments.ParcelParam, Parcel(1, "test"))

                arguments {
                    Arguments.TextParam2.set("test")
                    Arguments.SerialParam2.set(Serial(1, "test"))
                    Arguments.ParcelParam2.set(Parcel(1, "test"))
                }
            }
        }
    }
}

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class ActivityArgumentTest {
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
            Assert.assertEquals("test", activity.text2)
            Assert.assertNotEquals("test2", activity.text)
            Assert.assertNotEquals("test2", activity.text2)
            Assert.assertEquals(DirectArgumentActivity.Serial(1, "test"), activity.serial)
            Assert.assertEquals(DirectArgumentActivity.Serial(1, "test"), activity.serial2)
            Assert.assertEquals(DirectArgumentActivity.Parcel(1, "test"), activity.parcel)
            Assert.assertEquals(DirectArgumentActivity.Parcel(1, "test"), activity.parcel2)
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
    fun `reading nonNull arguments with default value should return default if not provided`() {
        scenario.onActivity { activity ->
            Assert.assertEquals("default", activity.notProvidedDefault)
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