package com.source.bricks.entry.archive


/**
 * Holds history for [T] model old implementations and current version of the latest model based on
 * [EntryChain] entries.
 *
 * Use [archiveOf] to obtain instance of [EntryArchive] as it has no public constructors so that
 * all correct chain is stored in the archive.
 *
 * @see EntryChain
 * */
class EntryArchive<T> @PublishedApi internal constructor(
        private val chain: EntryChain<T>
) {
    /**
     * Current version of [T]
     * */
    val version = chain.outputVersion

    /**
     * Returns all the necessary transformations to obtain current implementation of [T] or null
     * if the [version] is greater than any operation version in the chain.
     * */
    fun findRecoveryPath(version: Long): EntryRecoveryPath<T>? {
        return chain.getApplicableChain(version)
    }

    override fun toString(): String {
        return "EntryArchive(chain=$chain, version=$version)"
    }

    companion object {
        /**
         * Provides [EntryArchive] using [body] to setup [EntryChain] builder.
         *
         * @see EntryChain
         * */
        inline fun <reified T> archiveOf(body: EntryChain.Builder.() -> Unit = {}): EntryArchive<T> {
            return EntryArchive(
                    chain = EntryChain.Builder().apply(body).build()
            )
        }
    }
}