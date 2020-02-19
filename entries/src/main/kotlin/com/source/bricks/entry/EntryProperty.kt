package com.source.bricks.entry

import kotlin.properties.ReadWriteProperty

interface EntryProperty<T> : ReadWriteProperty<Any?, T> {
    fun remove()
}