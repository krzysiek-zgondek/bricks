package com.source.bricks.dispatcher

/**
 * @project Bricks
 * @author SourceOne on 15.01.2020
 */

/**
 * Simple object dispatcher
 * Use builder to create immutable list of dispatch receivers
 * */
class Dispatcher<T> internal constructor(private val receivers: List<(T) -> Unit>) {
    class Builder<T> {
        private val builderList: MutableList<(T) -> Unit> = mutableListOf()

        fun add(callback: (T) -> Unit) {
            builderList.add(callback)
        }

        fun build(): Dispatcher<T> {
            return Dispatcher(builderList.toList())
        }

        companion object{
            inline fun <T> build(body: Builder<T>.() -> Unit): Dispatcher<T> {
                return Builder<T>().apply(body).build()
            }
        }
    }

    fun dispatch(dispatch: T) {
        receivers.forEach { callback -> callback(dispatch) }
    }

    inline fun dispatch(factory: () -> T) {
        dispatch(factory())
    }
}
