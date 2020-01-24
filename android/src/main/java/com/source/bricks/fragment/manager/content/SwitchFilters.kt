package com.source.bricks.fragment.manager.content

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * Collection of filters for [FragmentManager]'s fragments
 * All filters use [FragmentManager.getFragments] method to obtain
 * list of fragments. This method provides only those fragments which
 * are currently running inside [FragmentManager] (meaning no detached
 * fragments, nor backstack)
 * */
@Suppress("FunctionName")
object SwitchFilters {

    /**
     * This filter provides either all fragments currently running inside
     * [FragmentManager] or only those which are added to the container.
     * Behavior depends on [descriptor]'s containerOnly parameter.
     * */
    inline fun <reified T : Fragment> filterContainer(
        fragmentManager: FragmentManager, descriptor: FragmentDescriptor<T>
    ): List<Fragment> {
        val lookupFragments = fragmentManager.fragments

        return if (descriptor.containerOnly)
            lookupFragments.filter { fragment -> fragment.id == descriptor.container }
        else lookupFragments
    }

    /**
     * This filter provides only those fragments which have same tag
     * as provided in [descriptor]'s tag parameter.
     * */
    inline fun <reified T : Fragment> filterTag(
        fragmentManager: FragmentManager, descriptor: FragmentDescriptor<T>
    ): List<Fragment> {
        return filterContainer(fragmentManager, descriptor)
            .filter { fragment -> fragment.tag == descriptor.tag }
    }

    /**
     * This filter provides only those fragments which have same class
     * as provided in [descriptor]'s instanceCls parameter.
     * */
    inline fun <reified T : Fragment> filterClass(
        fragmentManager: FragmentManager, descriptor: FragmentDescriptor<T>
    ): List<T> {
        return filterContainer(fragmentManager, descriptor)
            .filterIsInstance<T>()
    }

    /**
     * This filter provides only those fragments which have same class
     * and same tah as provided in [descriptor]'s instanceCls and tag parameters.
     * */
    inline fun <reified T : Fragment> filterClassTag(
        fragmentManager: FragmentManager, descriptor: FragmentDescriptor<T>
    ): List<T> {
        return filterContainer(fragmentManager, descriptor)
            .filterIsInstance<T>()
            .filter { fragment -> fragment.tag == descriptor.tag }
    }
}