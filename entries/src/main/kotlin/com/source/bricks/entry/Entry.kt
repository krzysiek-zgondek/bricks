package com.source.bricks.entry

data class Entry<T>(val version: Long = 0, val data: T)