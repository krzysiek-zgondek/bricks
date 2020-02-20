package com.source.bricks.entry

import com.source.bricks.entry.archive.EntryArchive
import com.source.bricks.entry.archive.EntryArchive.Companion.archiveOf

/**
 * Fully describes entry
 *
 * @param id entry id
 * @param archive history of entry model [T] updates, recovers old [T] after migration
 * @param factory creates new model instances [T], provides default values
 * */
data class EntryDescriptor<T>(
    val id: Any,
    val archive: EntryArchive<T>,
    val factory: () -> T
)


/**
 * Creates [EntryDescriptor]
 *
 * @param id any object
 * @param archive use [archiveOf] to create entry updates records
 * @param factory either lambda or constructor reference

 * @see [EntryDescriptor]
 * */
inline fun <reified T> define(
    id: Any = EntryKey.defaultId<T>(),
    archive: EntryArchive<T> = archiveOf(),
    noinline factory: () -> T = ::kotlinInstance
): EntryDescriptor<T> {
    return EntryDescriptor(id, archive, factory)
}
