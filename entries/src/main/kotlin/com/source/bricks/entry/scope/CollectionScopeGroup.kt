package com.source.bricks.entry.scope


/**
 * Basic implementation of [EntryScopeGroup].
 * */
class CollectionScopeGroup(private val scopes: MutableCollection<EntryScope>) : EntryScopeGroup {
    override fun add(scope: EntryScope) {
        scopes += scope
    }

    override fun remove(scope: EntryScope) {
        scopes -= scope
    }

    override fun clearAllScopes() {
        scopes.forEach { scope -> scope.clear() }
    }
}


/**
 * Creates scope group using [MutableList] for registry. Change [list] to any synchronized implementation
 * if you want thread safety ie. [java.util.Collections.synchronizedList]
 * */
@Suppress("NOTHING_TO_INLINE")
inline fun listedScopeGroup(list: MutableList<EntryScope> = mutableListOf()): EntryScopeGroup {
    return CollectionScopeGroup(list)
}