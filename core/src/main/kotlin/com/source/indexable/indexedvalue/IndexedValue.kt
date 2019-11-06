package com.source.indexable.indexedvalue

import com.source.indexable.Indexable

/**
 * Binds value of type [Type] with index of type [Id]
 * */
class IndexedValue<Id, Type>(
    index: Id, val value: Type
) : Indexable<Id> {
    override val id: Id = index
}

/**
 * Create object which binds value of type [Type] with index of type [Id]
 * */
@Suppress("NOTHING_TO_INLINE")
inline infix fun <Id, Type> Type.index(id: Id): IndexedValue<Id, Type> {
    return IndexedValue(id, this)
}