package com.source.bricks.entry.scope

import com.source.bricks.entry.Entry
import com.source.bricks.entry.EntryKey
import com.source.bricks.entry.transcoder.EntryTranscoder
import com.source.bricks.storage.Storage
import com.source.bricks.storage.map.hashMapStorage

/**
 * Provides [EntryScope] implementations
 * */
inline fun <reified RawType> scope(
    transcoder: EntryTranscoder<RawType>,
    storage: Storage<EntryKey, Entry<RawType>?> = hashMapStorage()
): EntryScope {
    return DefaultScope(
        transcoder = transcoder,
        storage = storage
    )
}