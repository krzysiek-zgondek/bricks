package com.source.bricks.fragment.manager.content

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * Shorthand for lambda receiver declaration.
 *
 * newInstance - true if fragment during [SwitchTransaction] was spawned
 * otherwise false
 * */
typealias BackStackTransaction<T> = (
    manager: FragmentManager,
    transaction: FragmentTransaction,
    descriptor: FragmentDescriptor<T>,
    newInstance: Boolean
) -> Unit