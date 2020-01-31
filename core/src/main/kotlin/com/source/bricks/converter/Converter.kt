package com.source.bricks.converter


interface Converter<From, To> {
    fun from(raw: From?): To?
    fun to(data: To?): From?
}