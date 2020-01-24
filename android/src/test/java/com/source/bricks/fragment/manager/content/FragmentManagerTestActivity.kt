package com.source.bricks.fragment.manager.content

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * @author SourceOne on 24.01.2020
 * @project Bricks
 */


class FragmentManagerTestActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply {
            id = rootId

            val container1 = LinearLayout(context)
                .apply { id = container1 }
            val container2 = LinearLayout(context)
                .apply { id = container2 }

            addView(container1)
            addView(container2)
        }

        setContentView(root)
    }

    companion object {
        @IdRes
        val rootId = View.generateViewId()
        @IdRes
        val container1 = View.generateViewId()
        @IdRes
        val container2 = View.generateViewId()
    }
}