package com.source.resource

/**
 * @project Bricks
 * @author Source - Krzysztof Zgondek on 24.10.2019
 */

/**
 * Object which encapsulates state of retrieved value / data
 **/
sealed class Resource<Type> {
    /**
     * Represents successfully created data of type [Type]
     * */
    class Data<Type>(inline val value: Type) : Resource<Type>()

    /**
     * Represents exception thrown while creating data of type [Type]
     * */
    class Error<Type>(inline val value: Throwable) : Resource<Type>()

    companion object {
        /**
         * Shorthand for creating successful resource
         * */
        @JvmStatic
        @Suppress("NOTHING_TO_INLINE")
        inline fun <Type> result(value: Type): Data<Type> {
            return Data(value)
        }

        /**
         * Shorthand for creating error holder for the resource
         * */
        @JvmStatic
        @Suppress("NOTHING_TO_INLINE")
        inline fun <Type> error(value: Throwable): Error<Type> {
            return Error(value)
        }
    }
}

/**
 * Creates a resource of type [Type] from a declaration
 * */
inline fun <Type> resource(declaration: () -> Type): Resource<Type> {
    return try {
        Resource.result(declaration())
    } catch (exception: Exception) {
        Resource.error(exception)
    }
}