package com.source.bricks.entry

import kotlin.properties.ReadWriteProperty


/**
 * Entry property delegation description
 * */
interface EntryProperty<T> : ReadWriteProperty<Any?, T> {
    /**
     * Removes entry from scope
     * */
    fun remove()
}