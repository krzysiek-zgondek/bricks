package com.source.bricks.entry.transcoder

import com.source.bricks.reflect.tClass

interface EntryDecoder<Raw> {
    fun <Type> decode(raw: Raw, cls: TClass<Type>): Type?
}