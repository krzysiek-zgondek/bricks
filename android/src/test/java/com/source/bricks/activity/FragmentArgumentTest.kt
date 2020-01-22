package com.source.bricks.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import com.source.bricks.intent.intentFor
import com.source.bricks.fragment.argument.argument
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

class FragmentArgumentTestActivity : FragmentActivity() {
    data class Serial(val value: Int, val text: String) : Serializable
    @Parcelize
    data class Parcel(val value: Int, val text: String) : Parcelable

    class ArgumentTestFragment : Fragment() {
        var text by argument(default = "")
        var serial by argument<Serial>()
        var parcel by argument<Parcel>()
        var notProvided by argument<Parcel>()

        companion object {
            fun getInstance(): ArgumentTestFragment {
                return ArgumentTestFragment().apply {
                    text = "test"
                    serial = Serial(1, "test")
                    parcel = Parcel(1, "test")
                }
            }
        }
    }

    lateinit var testFragment: ArgumentTestFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction().run {
                add(ArgumentTestFragment.getInstance(), null)
            }.commit()
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        testFragment = fragment as ArgumentTestFragment
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return context.intentFor<FragmentArgumentTestActivity>()
        }
    }
}

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class FragmentArgumentTest {
    lateinit var scenario: ActivityScenario<FragmentArgumentTestActivity>

    @Before
    fun setUp() {
        addTestActivities(
            FragmentArgumentTestActivity::class
        )

        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun `provide arguments and check if they were obtained properly`() {
        scenario.onActivity { activity ->
            activity.supportFragmentManager.executePendingTransactions()
            Assert.assertEquals("test", activity.testFragment.text)
            Assert.assertNotEquals("test2", activity.testFragment.text)
            Assert.assertEquals(FragmentArgumentTestActivity.Serial(1, "test"), activity.testFragment.serial)
            Assert.assertEquals(FragmentArgumentTestActivity.Parcel(1, "test"), activity.testFragment.parcel)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `reading nonNull arguments which were not provided should throw`() {
        scenario.onActivity { activity ->
            val provided = activity.testFragment.notProvided
            provided.text.hashCode()
        }
    }

    @Test
    fun `change argument and check if value was properly stored`() {
        scenario.onActivity { activity ->
            activity.testFragment.text = "test 2"
            Assert.assertEquals("value should be changed right away", "test 2", activity.testFragment.text)
        }

        scenario.recreate()
        scenario.onActivity { activity ->
            Assert.assertEquals(
                "value should still be changed after recreation",
                "test 2",
                activity.testFragment.text
            )
        }
    }
}