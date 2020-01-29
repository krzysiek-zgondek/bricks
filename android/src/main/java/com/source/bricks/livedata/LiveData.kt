package com.source.bricks.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


/**
 * Adds [receiver] as an observer for provided [LiveData] and removes it
 * after first value delivery (can be either new or old [LiveData] value).
 *
 * This version uses [LiveData.observeForever] method to register observer.
 *
 * */
inline fun <reified T> LiveData<T>.observeOnce(crossinline receiver: (T) -> Unit) {
    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            receiver(value)
            removeObserver(this)
        }
    }

    observeForever(observer)
}


/**
 * Adds [receiver] as an observer for provided [LiveData] and removes it
 * after first value delivery (can be either new or old [LiveData] value).
 * */
inline fun <reified T> LiveData<T>.observeOnce(owner: LifecycleOwner, crossinline receiver: (T) -> Unit) {
    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            receiver(value)
            removeObserver(this)
        }
    }

    observe(owner, observer)
}