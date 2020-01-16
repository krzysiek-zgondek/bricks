package com.source.bricks.activity

import com.source.core.dispatcher.Dispatcher

/**
 * Dispatching for [android.app.Activity.onActivityResult]
 * */
typealias ResultDispatcher = Dispatcher<ResultDispatch>

inline fun buildResultDispatcher(body: Dispatcher.Builder<ResultDispatch>.() -> Unit): ResultDispatcher {
    return Dispatcher.Builder.build(body)
}
