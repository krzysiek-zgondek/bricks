package com.source.bricks.test

import org.mockito.Mockito

/**
 * Fixes java.lang.IllegalStateException when [Mockito.any()] returns null
 * */
fun <T> any(): T = Mockito.any<T>()

/**
 * Creates mocks using provided reified [T].
 * This enables to infer the type or provide it using shorter
 * form than T::class.java
 * */
inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

/**
 * Invokes [function] with mocked arguments and returns them.
 * @param mock1 uses default [mock] implementation or provided
 * @param initialize initializes mocks before execution of [function]
 * @return mocks created before execution for later inspection
 * */
inline fun <reified T, R> mockCall1(
    function: (T) -> R,
    mock1: T = mock(),
    initialize: (T) -> Unit = { }
): T {
    initialize(mock1)
    function.invoke(mock1)
    return mock1
}

/**
 * Invokes [function] with mocked arguments and returns them.
 * @param mock1 uses default [mock] implementation or provided
 * @param mock2 uses default [mock] implementation or provided
 * @param initialize initializes mocks before execution of [function]
 * @return mocks created before execution for later inspection
 * */
inline fun <reified T, reified U, R> mockCall2(
    function: (T, U) -> R,
    mock1: T = mock(),
    mock2: U = mock(),
    initialize: (T, U) -> Unit = { _, _ -> }
): Pair<T, U> {
    initialize(mock1, mock2)
    function.invoke(mock1, mock2)
    return mock1 to mock2
}