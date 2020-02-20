package com.source.bricks.entry.scope.group

import com.source.bricks.entry.scope.EntryScope

interface EntryScopeGroup {
    /**
     * Add provided [scope] to group
     * */
    fun add(scope: EntryScope)

    /**
     * Removes provided [scope] from group
     * */
    fun remove(scope: EntryScope)

    /**
     * Clears all data stored in all registered scopes
     * */
    fun clearAllScopes()
}


inline fun <T : EntryScope> EntryScopeGroup.register(provider: () -> T): T {
    return provider().also(::add)
}