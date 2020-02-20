package com.source.bricks.entry.transcoder

import com.source.bricks.reflect.TClass


/**
 * Encodes input object
 * */
interface EntryEncoder<Raw> {
    fun <Type> encode(input: Type, cls: TClass<Type>): Raw?
}