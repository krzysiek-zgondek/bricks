package com.source.bricks.entry.archive

import com.source.bricks.reflect.TClass
import com.source.bricks.reflect.tClass


/**
 * Describes update operation for [EntryChain]
 * @param version entry's historical version that [apply] converts to
 * @param inputCls expected class of input object
 * @param outputCls class that [apply] converts into
 * @param apply converts objects of class [inputCls] into class [outputCls]
 * */
data class EntryUpdate(
    val version: Long,
    val inputCls: TClass<*>,
    val outputCls: TClass<*>,
    val apply: (Any) -> Any
)

/**
 * Entry update factory
 * */
@Suppress("UNCHECKED_CAST")
inline fun <reified T, reified R> entryUpdate(
    version: Long,
    noinline operation: (T) -> R
): EntryUpdate {
    return EntryUpdate(
        version = version,
        inputCls = T::class.tClass,
        outputCls = R::class.tClass,
        apply = operation as (Any) -> Any
    )
}