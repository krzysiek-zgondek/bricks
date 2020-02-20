package com.source.bricks.entry.transcoder

import com.source.bricks.reflect.TClass


/**
 * Decode data encoded by [EntryEncoder]
 * */
interface EntryDecoder<Raw> {
    fun <Type> decode(raw: Raw, cls: TClass<Type>): Type?
}