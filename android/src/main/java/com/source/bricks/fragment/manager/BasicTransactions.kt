package com.source.bricks.fragment.manager

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit

/**
 * @project Bricks
 * @author SourceOne on 22.01.2020
 */

inline fun <reified Manager : FragmentManager> Manager.replace(
    @IdRes containerId: Int,
    tag: String? = null,
    backStackAllowed: Boolean = false,
    factory: () -> Fragment
) = commit {
    replace(containerId, factory(), tag)
    if (backStackAllowed)
        addToBackStack(tag)
}