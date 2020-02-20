package com.source.bricks.fragment.manager.content

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * Shorthand for lambda receiver declaration.
 * [SwitchTransaction] should return true if new instance should
 * be created and added to [FragmentManager] otherwise
 * */
typealias SwitchTransaction<T> = (
    manager: FragmentManager,
    transaction: FragmentTransaction,
    descriptor: FragmentDescriptor<T>
) -> Boolean