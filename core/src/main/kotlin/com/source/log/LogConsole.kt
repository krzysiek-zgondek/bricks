package com.source.log

/**
 * @project Bricks
 * @author SourceOne on 07.11.2019
 */

@PublishedApi
internal const val emptyTag = ""

@PublishedApi
@Suppress("NOTHING_TO_INLINE")
internal inline fun decorate(tag: Any?): String {
    return emptyTag.takeIf { tag == null } ?: "$tag: "
}

@Suppress("NOTHING_TO_INLINE")
inline fun Any.log(tag: Any? = null) {
//    println(this)
    println("${System.nanoTime()} ${Thread.currentThread().name} ${decorate(tag)}$this")
}