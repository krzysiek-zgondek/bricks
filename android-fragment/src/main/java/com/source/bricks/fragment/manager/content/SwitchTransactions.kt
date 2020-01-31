package com.source.bricks.fragment.manager.content

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * Transactions which can be used to manipulate behavior of
 * [com.source.bricks.fragment.manager.switchTo] extension function.
 * SwitchTransaction is the main transaction of switch operation.
 * */
@Suppress("FunctionName")
object SwitchTransactions {

    /**
     * Allows to create and add new fragment to [FragmentManager]
     * */
    inline fun <reified T : Fragment> Add(
        manager: FragmentManager,
        transaction: FragmentTransaction,
        descriptor: FragmentDescriptor<T>
    ): Boolean = true

    /**
     * If fragment specified in the [descriptor] already exists
     * it will be removed and new instance will be spawned
     * and added to [FragmentManager]
     * */
    inline fun <reified T : Fragment> Replace(
        manager: FragmentManager,
        transaction: FragmentTransaction,
        descriptor: FragmentDescriptor<T>
    ): Boolean = with(transaction) {
        val last = SwitchFilters.filterClassTag(
            manager,
            descriptor
        ).lastOrNull()
        if (last != null)
            remove(last)

        true
    }

    /**
     * If fragment specified in the [descriptor] already exists
     * it will be shown again (if was hidden before) without
     * spawning new instance otherwise new instance will be spawned
     * and added to [FragmentManager]
     * */
    inline fun <reified T : Fragment> Recycle(
        manager: FragmentManager,
        transaction: FragmentTransaction,
        descriptor: FragmentDescriptor<T>
    ): Boolean = with(transaction) {
        val last = SwitchFilters.filterClassTag(
            manager,
            descriptor
        ).lastOrNull()
        if (last != null)
            show(last)

        last == null
    }
}