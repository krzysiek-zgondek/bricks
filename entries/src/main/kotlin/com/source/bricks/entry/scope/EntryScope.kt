package com.source.bricks.entry.scope

import com.source.bricks.entry.EntryDescriptor
import com.source.bricks.entry.EntryKey
import com.source.bricks.entry.Entry
import com.source.bricks.reflect.TClass


/**
 * Base interface defining scope for [Entry] storage and interaction
 * */
interface EntryScope {
    /**
     * All entries saved within Scope's storage
     * */
    val entries: List<EntryKey>

    /**
     * Returns entry which matches [descriptor] or null
     * */
    fun <T> get(descriptor: EntryDescriptor<out T>, cls: TClass<T>): T?

    /**
     * Stores [value] using [descriptor]
     * */
    fun <T> set(descriptor: EntryDescriptor<out T>, value: T, cls: TClass<T>)

    /**
     * Removes first entry that matches [descriptor] or do nothing if entry was not found
     * */
    fun <T> remove(descriptor: EntryDescriptor<out T>)

    /**
     * Removes all entries from Scope's storage
     * */
    fun clear()
}


