package com.source.bricks.entry.transcoder

import com.source.bricks.reflect.tClass

interface EntryEncoder<Raw> {
    fun <Type> encode(input: Type, cls: TClass<Type>): Raw?
}