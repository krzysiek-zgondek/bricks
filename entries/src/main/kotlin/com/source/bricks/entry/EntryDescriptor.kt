package com.source.bricks.entry

import com.source.bricks.entry.archive.EntryArchive
import com.source.bricks.entry.archive.EntryArchive.Companion.archiveOf


data class EntryDescriptor<T>(
        val id: Any,
        val archive: EntryArchive<T>,
        val factory: () -> T
)

inline fun <reified T> define(
        id: Any = EntryKey.defaultId<T>(),
        archive: EntryArchive<T> = archiveOf(),
        noinline factory: () -> T = ::kotlinInstance
): EntryDescriptor<T> {
    return EntryDescriptor(id, archive, factory)
}
