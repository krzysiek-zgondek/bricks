package com.source.bricks.fragment.manager.content

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.source.bricks.fragment.transaction.collection.hide

/**
 * Transactions which can be used to manipulate behavior of
 * [com.source.bricks.fragment.manager.switchTo] extension function.
 * HideTransaction is the first transaction that occurs during
 * switch operation.
 * */
@Suppress("FunctionName")
object HideTransactions {

    /**
     * Do nothing to existing fragments
     * */
    inline fun <reified T : Fragment> None(
        manager: FragmentManager,
        transaction: FragmentTransaction,
        descriptor: FragmentDescriptor<T>
    ) = Unit

    /**
     * Hides all existing fragments in currently selected container
     * */
    inline fun <reified T : Fragment> All(
        manager: FragmentManager,
        transaction: FragmentTransaction,
        descriptor: FragmentDescriptor<T>
    ) = with(transaction) {
        hide(
            SwitchFilters.filterContainer(
                manager,
                descriptor
            )
        )
    }

    /**
     * Hides fragments from currently selected container
     * which have same tag as [descriptor]'s tag
     * */
    inline fun <reified T : Fragment> Tag(
        manager: FragmentManager,
        transaction: FragmentTransaction,
        descriptor: FragmentDescriptor<T>
    ) = with(transaction) {
        hide(
            SwitchFilters.filterTag(
                manager,
                descriptor
            )
        )
    }

    /**
     * Hides fragments from currently selected container
     * which have same type as [descriptor]'s instanceCls
     * */
    inline fun <reified T : Fragment> Class(
        manager: FragmentManager,
        transaction: FragmentTransaction,
        descriptor: FragmentDescriptor<T>
    ) = with(transaction) {
        hide(
            SwitchFilters.filterClass(
                manager,
                descriptor
            )
        )
    }

    /**
     * Hides fragments from currently selected container
     * which have same type and tag as [descriptor]'s
     * instanceCls and tag
     * */
    inline fun <reified T : Fragment> ClassTag(
        manager: FragmentManager,
        transaction: FragmentTransaction,
        descriptor: FragmentDescriptor<T>
    ) = with(transaction) {
        hide(
            SwitchFilters.filterClassTag(
                manager,
                descriptor
            )
        )
    }
}