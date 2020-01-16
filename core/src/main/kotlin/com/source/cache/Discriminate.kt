package com.source.cache

object Discriminate {
    inline fun <reified T : Any> not(value: Any?): Boolean {
        return value !is T
    }

    inline fun never(): Boolean {
        return false
    }

}