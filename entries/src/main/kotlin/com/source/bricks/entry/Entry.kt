package com.source.bricks.entry


/**
 * Defines entries used in [com.source.bricks.entry.scope.EntryScope]
 *
 * @param version entry version (migrations)
 * @param data entry value
 * */
data class Entry<T>(
    val version: Long = 0,
    val data: T
)