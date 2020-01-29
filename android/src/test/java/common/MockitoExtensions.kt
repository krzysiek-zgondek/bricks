package common

import org.mockito.Mockito

/**
 * Creates mocks using provided reified [T].
 * This enables to infer the type or provide it using shorter
 * form than T::class.java
 * */
inline fun <reified T> mock(): T = Mockito.mock(T::class.java)


/**
 * Invokes [function] with mocked arguments and returns them.
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