package com.source.bricks.fragment.manager.content

import androidx.fragment.app.Fragment


/**
 * Holds results from switch operation performed by
 * [com.source.bricks.fragment.manager.switchTo].
 *
 * You can use it with combination of:
 *  [com.source.bricks.fragment.manager.whenAvailable] or
 *  [com.source.bricks.fragment.manager.whenRecycled]
 * to perform an action on fragment used in switch operation.
 *
 * @param descriptor - descriptor used during switch operation
 * @param newInstance - true if new fragment instance was spawned
 *  during switch operation
 *  */
data class SwitchResult<T : Fragment>(
    val descriptor: FragmentDescriptor<T>,
    val newInstance: Boolean
)