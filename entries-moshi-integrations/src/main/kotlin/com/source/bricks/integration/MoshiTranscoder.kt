@file:Suppress("SpellCheckingInspection")

package com.source.bricks.integration

import com.source.bricks.entry.transcoder.EntryTranscoder
import com.source.bricks.reflect.TClass
import com.source.bricks.reflect.java
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


internal val moshi: Moshi by lazy {
    Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
}

@Suppress("UNCHECKED_CAST", "FunctionName")
fun MoshiTranscoder(): EntryTranscoder<String> {
    return object : EntryTranscoder<String> {
        override fun <Type> decode(raw: String, cls: TClass<Type>): Type? {
            return moshi.adapter(cls.java).fromJson(raw)

        }

        override fun <Type> encode(input: Type, cls: TClass<Type>): String? {
            return moshi.adapter(cls.java).toJson(input)
        }
    }
}