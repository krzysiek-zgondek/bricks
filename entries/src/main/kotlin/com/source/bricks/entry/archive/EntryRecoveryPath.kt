package com.source.bricks.entry.archive

import com.source.bricks.reflect.TClass


/**
 * Holds successive operations that converts old implementation of [T] into current
 *
 * @see apply
 * */
class EntryRecoveryPath<T> @PublishedApi internal constructor(
        private val operations: List<EntryUpdate>
) {
    /**
     * Expected class of input object
     * */
    val inputCls: TClass<*> = operations.first().inputCls
    /**
     * Expected class of output object
     * */
    val outputCls: TClass<*> = operations.last().outputCls

    /**
    * Applies transformation to [old] and returns updated object of type [T]
    * @throws IllegalArgumentException if old is not instance of [inputCls]
    * @throws ClassCastException if new is not instance of [outputCls]
    * */
    fun apply(old: Any): T {
        if(inputCls.kClass != old::class)
            throw IllegalArgumentException("Bad input. Found: ${old::class} but expected: ${inputCls}")
        /*
        * this class can only be created directly from [UpdateChain] object which checks if chain is not broken
        * and return [T] type in the end
        * */
        @Suppress("UNCHECKED_CAST")
        return operations.fold(old) { current, operation ->
            operation.apply(current)
        } as T
    }

    companion object {
        /**
         * Factory method for obtaining [EntryRecoveryPath] object.
         * Internal use only
         * */
        internal fun <T> from(operations: List<EntryUpdate>): EntryRecoveryPath<T>? {
            if (operations.isEmpty())
                return null

            return EntryRecoveryPath(operations)
        }
    }
}