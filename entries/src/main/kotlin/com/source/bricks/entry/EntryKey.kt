package com.source.bricks.entry


/**
 * Used as entry identifier in scope's
 *
 * @param id used as a key in scope's storage
 *
 * @see [com.source.bricks.entry.scope.EntryScope]
 * */
data class EntryKey(val id: Any) {

    /**
     * Default id provider for defining [EntryDescriptor] id
     * */
    companion object {
        inline fun <reified T> defaultId(): Any = T::class
    }
}