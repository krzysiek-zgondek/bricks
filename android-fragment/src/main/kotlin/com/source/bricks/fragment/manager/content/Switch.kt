package com.source.bricks.fragment.manager.content

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * Performs switch operation on [FragmentManager].

 * Switch operation combines:
 *  - hide operation described by [hideTransaction]. @see [HideTransactions]
 *  - remove/hide/add/show operation described by [switchTransaction] @see [SwitchTransactions]
 *  - backstack addition described by [backStackTransaction] @see [BackStackTransactions]
 *
 * If [containerOnly] value is true only fragments related to the
 * [containerId] view will be processed. Otherwise all fragments
 * from [FragmentManager.getFragments] will be processed.
 *
 *  */
inline fun <reified T : Fragment> FragmentManager.switchTo(
    hideTransaction: HideTransaction<T> = HideTransactions::All,
    switchTransaction: SwitchTransaction<T> = SwitchTransactions::Recycle,
    backStackTransaction: BackStackTransaction<T> = BackStackTransactions::Always,
    @IdRes containerId: Int = 0,
    containerOnly: Boolean = true,
    tag: String? = T::class.java.name,
    provider: () -> T
): SwitchResult<T> {
    val descriptor = fragmentDescriptor<T>(
        containerId, containerOnly, tag
    )

    return internalSwitchTo(
        this,
        hideTransaction,
        switchTransaction,
        backStackTransaction,
        provider,
        descriptor
    )
}

/**
 * Current implementation of switch operation.
 * Internal use only
 * */
@PublishedApi
internal inline fun <reified T : Fragment> internalSwitchTo(
    manager: FragmentManager,
    hideTransaction: HideTransaction<T> = HideTransactions::All,
    switchTransaction: SwitchTransaction<T> = SwitchTransactions::Recycle,
    backStackTransaction: BackStackTransaction<T> = BackStackTransactions::Always,
    provider: () -> T,
    descriptor: FragmentDescriptor<T>
): SwitchResult<T> {
    val transaction = manager.beginTransaction()

    hideTransaction(manager, transaction, descriptor)

    val newInstance = switchTransaction(manager, transaction, descriptor)
    if (newInstance) {
        transaction.add(descriptor.container, provider(), descriptor.tag)
    }

    backStackTransaction(manager, transaction, descriptor, newInstance)

    transaction.commit()

    return SwitchResult(
        descriptor,
        newInstance
    )
}

