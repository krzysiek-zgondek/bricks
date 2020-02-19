package com.source.bricks.entry


data class EntryKey(val id: Any) {
    companion object {
        inline fun <reified T> defaultId(): Any = T::class
    }
}