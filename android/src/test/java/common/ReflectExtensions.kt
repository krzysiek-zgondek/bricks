package common

/**
 * @project Bricks
 * @author SourceOne on 24.01.2020
 */



inline fun <reified T> forceFieldValue(descriptor: T, name: String, value: Any?) {
    val field = T::class.java.getDeclaredField(name)
    field.isAccessible = true
    field.set(descriptor, value)
}