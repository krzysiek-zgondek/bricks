package com.source.bricks.entry.archive

import com.source.bricks.entry.archive.EntryChain.Builder
import kotlin.reflect.KClass


/**
 * Holds operations of how to transform older versions of object [T] into current [T].
 * Operations are stored in [EntryChain] from the oldest to current one so that you can transform
 * object from whatever previous state you are holding into the current implementation.
 *
 * No public constructors so that all the conditions are met to have properly formed chain.
 * Use [Builder] instead.
 *
 * @see Builder
 * @see EntryArchive.archiveOf
 * */
class EntryChain<T> @PublishedApi internal constructor(
    internal val operations: List<EntryUpdate>
) {
    /**
     * Chain's output version.
     * */
    val outputVersion: Long = operations.lastOrNull()?.version ?: 0

    /**
     * Returns chain starting from operation that transforms [version] into newer implementation up
     * until [outputVersion] implementation (current)
     * */
    fun getApplicableChain(version: Long): EntryRecoveryPath<T>? {
        val first = operations.indexOfFirst { version < it.version }
        if (first == -1)
            return null

        return EntryRecoveryPath.from(
            operations.subList(first, operations.size)
        )
    }

    override fun toString(): String {
        return "UpdateChain(operations=$operations, outputVersion=$outputVersion)"
    }

    /**
     * Builder that constrains creation of [EntryChain] so it is properly formed. That means:
     *  - Input class of new operation has to be the same as the output class of previous one
     *  - Last operation has to have the same output class as [T] parameter provided in [build]
     *  - [lastVersion] is never decremented
     *
     * @see entry
     * */
    class Builder {
        /**
         * List of chain links
         * */
        @PublishedApi
        internal var operations: List<EntryUpdate> = emptyList()

        @PublishedApi
        internal inline val lastOperation: EntryUpdate?
            get() = operations.lastOrNull()

        /**
         * Version of last element in currently build chain or 0 if no entries.
         * Always value >= 0
         * */
        val lastVersion: Long
            get() = lastOperation?.version ?: 0L

        /**
         * Next expected version value
         * @see entry
         * */
        val nextVersion: Long
            get() = lastVersion + 1

        /**
         * Stores [operation] if constraints described here [Builder] are met.
         * @throws IllegalArgumentException when version is decremented
         * @throws IllegalArgumentException when chain is broken
         * */
        inline fun <reified In : Any, reified Out : Any> entry(
            version: Long = nextVersion,
            noinline operation: (In) -> Out
        ) {
            throwOnVersionDecrement(version)
            throwOnBrokenChain(In::class)

            operations = operations + entryUpdate(version, operation)
        }

        @PublishedApi
        internal fun throwOnVersionDecrement(value: Long) {
            if (lastVersion >= value)
                throw IllegalArgumentException("Version must be always set to higher number")
        }

        @PublishedApi
        internal fun throwOnBrokenChain(expected: KClass<*>) {
            if (isChainBroken(expected))
                throw IllegalArgumentException(
                    "Chain broken.\nExpected: $expected\nReceived: ${lastOperation?.outputCls}"
                )
        }

        @PublishedApi
        internal fun throwOnBrokenChainEnd(expected: KClass<*>) {
            if (isChainBroken(expected))
                throw IllegalStateException(
                    "Last element in the archive has to return object of type: $expected. Found: ${lastOperation?.outputCls}"
                )
        }

        /**
         * Returns true if [lastOperation] output class is same as [expected]
         * */
        private fun isChainBroken(expected: KClass<*>): Boolean {
            val lastOperation = lastOperation ?: return false
            val lastOutput = lastOperation.outputCls
            return lastOutput.kClass != expected
        }

        /**
         * Constrains creation of [EntryChain] so that chain is properly formed.
         *
         * @see Builder for more information on constrains
         * */
        inline fun <reified T> build(): EntryChain<T> {
            throwOnBrokenChainEnd(T::class)

            return EntryChain(operations)
        }
    }
}