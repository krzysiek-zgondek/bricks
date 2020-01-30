package common

/**
 * @project Bricks
 * @author SourceOne on 24.01.2020
 */


/**
 * Uses java reflection to omit kotlin npe for creating random data input.
 * */
inline fun <reified T> forceFieldValue(descriptor: T, name: String, value: Any?) {
    val field = T::class.java.getDeclaredField(name)
    field.isAccessible = true
    field.set(descriptor, value)
}