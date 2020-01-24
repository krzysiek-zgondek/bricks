package com.source.bricks.fragment.manager.content

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

/**
 * @project Bricks
 * @author SourceOne on 23.01.2020
 */

/**
 * Helper class for holding switch operation parameters.
 *
 * @property container holds id of view container where the transaction takes place
 * @property containerOnly use only fragments from the container
 * @property tag fragment identifier
 * @property instanceCls fragment type
 *
 * @constructor create switch operation descriptor
 *
 * */
data class FragmentDescriptor<T : Fragment>(
    @IdRes val container: Int = 0,
    val containerOnly: Boolean = true,
    val tag: String? = null,
    val instanceCls: KClass<T>
) {
    /**
     * Constructs descriptor using [Class.getName] of [instanceCls] as [tag] value
     * */
    constructor(
        @IdRes container: Int = 0,
        containerOnly: Boolean = true,
        instanceCls: KClass<T>
    ) : this(container, containerOnly, instanceCls.java.name, instanceCls)
}

/**
 * Helper function that extracts [T] as [KClass] and builds [FragmentDescriptor]

 * @param container holds id of view container where the transaction takes place
 * @param containerOnly use only fragments from the container
 * @param tag fragment identifier, defaults to [Class.getName] of [T]
 *
 * */
inline fun <reified T : Fragment> fragmentDescriptor(
    @IdRes container: Int = 0,
    containerOnly: Boolean = true,
    tag: String? = T::class.java.name
): FragmentDescriptor<T> {
    return FragmentDescriptor(container, containerOnly, tag, T::class)
}