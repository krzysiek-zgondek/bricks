package com.source.bricks.fragment.manager.content

import android.os.Build
import androidx.fragment.app.Fragment
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class FragmentDescriptorKtTest {
    class TestFragment : Fragment()

    @Test
    fun `default parameters should produce tag equal to instanceCls class name`() {
        val descriptor = fragmentDescriptor<TestFragment>()

        assertEquals(TestFragment::class.java.name, descriptor.tag)
        assertEquals(TestFragment::class, descriptor.instanceCls)
    }

    @Test
    fun `secondary constructor should produce tag equal to instanceCls class name`() {
        val descriptor = FragmentDescriptor(
            instanceCls = TestFragment::class
        )

        assertEquals(TestFragment::class.java.name, descriptor.tag)
        assertEquals(TestFragment::class, descriptor.instanceCls)
    }

}