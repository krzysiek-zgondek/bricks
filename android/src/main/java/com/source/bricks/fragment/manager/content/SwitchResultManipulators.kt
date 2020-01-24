package com.source.bricks.fragment.manager.content

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * Executes action only if fragment described in [result] descriptor is already
 * active and was not spawned during switch operation. If new instance of
 * fragment was just spawned use [whenAvailable] extension function.
 *
 * To find fragment in [FragmentManager.getFragments] it uses
 * [SwitchFilters.filterClassTag] filter which means it both looks for
 * match in type and tag and uses last one from the list (newest).
 *
 * If you want to perform action on all fragments found use [whenActiveClass].
 * */
inline fun <reified T : Fragment> FragmentManager.whenRecycled(
    result: SwitchResult<T>,
    action: (T) -> Unit
) {
    if (result.newInstance)
        return

    val fragments =
        SwitchFilters.filterClassTag(this, result.descriptor)

    val fragment = fragments.lastOrNull() ?: return

    action(fragment)
}