package com.source.bricks.argument

import kotlin.reflect.KProperty

/**
 * @project Bricks
 * @author SourceOne on 16.01.2020
 */

interface Argument<Ref, Type> {
    val name: String

    operator fun getValue(thisRef: Ref, property: KProperty<*>): Type
    operator fun setValue(thisRef: Ref, property: KProperty<*>, value: Type)
}

