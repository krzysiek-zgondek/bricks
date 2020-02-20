@file:Suppress("SpellCheckingInspection")

package com.source.bricks.entry.scope

import com.source.bricks.entry.Entry
import com.source.bricks.entry.EntryDescriptor
import com.source.bricks.entry.EntryKey
import com.source.bricks.entry.archive.EntryArchive
import com.source.bricks.entry.transcoder.EntryTranscoder
import com.source.bricks.reflect.TClass
import com.source.bricks.storage.Storage
import com.source.bricks.storage.map.hashMapStorage


/*
* Future: SharedStorage integration
* Future: Caching entries
* Future: Observe entries changes [probably by observing storage]
* */

/**
 * Base implementation of [EntryScope].
 *
 * Holds entries stored in [storage] and transforms then into proper format using [transcoder].
 *
 * Storage is exposed so that user can decide what's the best way to store items ie. File,
 * SharedPreferences, Hashmap or if entries should be persistent or not. If scope is planned to be
 * used concurently then by providing synchronized storage implementation it can be safely used.
 *
 * Transcoder is exposed so that user can decide if objects should be stored ie.
 * using Json, as raw refereneces, Bundles etc.
 *
 * @param transcoder defines how to encode and decode entries so that they can be stored in [storage]
 * @param storage where items are stored
 * */
class DefaultScope<RawType>(
        private val transcoder: EntryTranscoder<RawType>,
        private val storage: Storage<EntryKey, Entry<RawType>?> = hashMapStorage()
) : EntryScope {
    /**
     * List of keys currently stored in storage
     * */
    override val entries: List<EntryKey>
        get() = storage.entries

    /**
     * Obtains object from storage using [descriptor] key.
     * If stored object is older version of [T] then tries to recover it using [EntryDescriptor.archive]
     * If stored object is newer version of [T] then throws [UnsupportedOperationException]
     * */
    override fun <T> get(descriptor: EntryDescriptor<out T>, cls: TClass<T>): T? {
        val key = EntryKey(descriptor.id)
        val entry = storage[key] ?: return null
        val rawValue = entry.data ?: return null

        return when {
            entry.version < descriptor.archive.version -> {
                recoverFromHistoryOrThrow(key, entry, descriptor.archive)
                    ?.also { updated -> set(descriptor, updated, cls) }
            }
            entry.version == descriptor.archive.version -> {
                transcoder.decode(rawValue, cls)
            }
            else -> {
                throw UnsupportedOperationException("Cannot read older entry from new one")
            }
        }
    }

    /**
     * Stores [value] in storage using [descriptor] key
     * */
    override fun <T> set(descriptor: EntryDescriptor<out T>, value: T, cls: TClass<T>) {
        val key = EntryKey(descriptor.id)
        val rawValue = transcoder.encode(value, cls)
        if (rawValue == null) {
            storage.remove(key)
        } else {
            storage[key] = Entry(descriptor.archive.version, rawValue)
        }
    }

    /**
     * Removes entry stored using [descriptor] key
     * */
    override fun <T> remove(descriptor: EntryDescriptor<out T>) {
        val key = EntryKey(descriptor.id)
        storage.remove(key)
    }

    /**
     * Clears underlaying storage
     * */
    override fun clear() {
        storage.clear()
    }

    /**
     * Obtains new version of [T] from older version using [EntryDescriptor.archive] to obtain
     * recovery path if available.
     *
     * @param key storage entry id
     * @param entry stored old entry data
     * @param archive history of all entry version and how to convert them
     * */
    private fun <T> recoverFromHistoryOrThrow(
        key: EntryKey,
        entry: Entry<RawType>,
        archive: EntryArchive<out T>
    ): T? {
        val path = archive.findRecoveryPath(entry.version)
                ?: throw IllegalArgumentException("Couldn't find any applicable updates for $key = [$entry]")

        return runCatching {
            transcoder.decode(entry.data, path.inputCls)?.let(path::apply)
        }.getOrElse { reason ->
            val error = IllegalStateException("Couldn't upgrade $key = [$entry] with provided archives: $archive")
            error.addSuppressed(reason)
            throw error
        }
    }
}