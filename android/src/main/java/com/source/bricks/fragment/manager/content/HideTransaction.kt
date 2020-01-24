package com.source.bricks.fragment.manager.content

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * Shorthand for lambda receiver declaration
 *
 * @param manager
 * */
typealias HideTransaction<T> = (
    manager: FragmentManager,
    transaction: FragmentTransaction,
    descriptor: FragmentDescriptor<T>
) -> Unit