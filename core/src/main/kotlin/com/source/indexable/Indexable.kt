package com.source.indexable

/**
 * @project Bricks
 * @author SourceOne on 24.10.2019
 */

/**
 * Declares that object has index value of type [Type]
 * */
interface Indexable<Id> {
    val id: Id
}