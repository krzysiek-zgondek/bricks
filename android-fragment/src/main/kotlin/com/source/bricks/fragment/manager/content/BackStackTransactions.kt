package com.source.bricks.fragment.manager.content

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * Transactions which can be used to manipulate behavior of
 * [com.source.bricks.fragment.manager.switchTo] extension function.
 * BackStackTransactions controls whether the switch operation will be
 * added to [FragmentManager] backstack or not.
 * */

@Suppress("FunctionName")
object BackStackTransactions {

    /**
     * Never adds transaction to backstack
     * */
    inline fun <reified T : Fragment> None(
        manager: FragmentManager,
        transaction: FragmentTransaction,
        descriptor: FragmentDescriptor<T>,
        newInstance: Boolean
    ): Unit = Unit

    /**
     * Adds transaction only if new fragment was spawned
     * during [SwitchTransaction]
     * */
    inline fun <reified T : Fragment> New(
        manager: FragmentManager,
        transaction: FragmentTransaction,
        descriptor: FragmentDescriptor<T>,
        newInstance: Boolean
    ): Unit = with(transaction) {
        if (newInstance)
            addToBackStack(descriptor.tag)
    }

    /**
     * Always adds transaction to backstack
     * */
    inline fun <reified T : Fragment> Always(
        manager: FragmentManager,
        transaction: FragmentTransaction,
        descriptor: FragmentDescriptor<T>,
        newInstance: Boolean
    ): Unit = with(transaction) {
        addToBackStack(descriptor.tag)
    }

    /**
     * Adds transaction to backstack only if during
     * [SwitchTransaction] new fragment was spawned.
     * In contrast to [New] it uses [Class.getName] of [T]
     * instead of [descriptor]'s tag
     * */
    inline fun <reified T : Fragment> NewClass(
        manager: FragmentManager,
        transaction: FragmentTransaction,
        descriptor: FragmentDescriptor<T>,
        newInstance: Boolean
    ): Unit = with(transaction) {
        if (newInstance)
            addToBackStack(T::class.java.name)
    }

    /**
     * Always adds transaction to backstack.
     * In contrast to [Always] it uses [Class.getName] of [T]
     * instead of [descriptor]'s tag
     * */
    inline fun <reified T : Fragment> AlwaysClass(
        manager: FragmentManager,
        transaction: FragmentTransaction,
        descriptor: FragmentDescriptor<T>,
        newInstance: Boolean
    ): Unit = with(transaction) {
        addToBackStack(T::class.java.name)
    }
}