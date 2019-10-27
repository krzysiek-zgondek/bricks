package com.source.provider.cached

import com.source.cache.cache
import com.source.provider.get
import com.source.provider.provider
import com.source.resource.Resource
import com.source.resource.resource
import com.source.storage.map.hashMapStorage
import java.io.File

//
//
//class Symbol
//
//enum class Init {
//    INIT;
//
//    companion object {
//        fun create(): Init = INIT
//    }
//
//}

//val Provider: Symbol = Symbol

//inline fun <reified T> using() = Unit

fun main() {
    val pr = provider { input: String ->
        println("provided: $input")
        resource {File(input).inputStream()}
    } using { input ->
        cache(
            source = cache(hashMapStorage()),
            successType = Resource.Error::class,
            provider = input
        )
    }

    val a = println( pr["asd1"])
    val a1 = println(resource { pr["asd2"] })
    val a2 = println(resource { pr["asd3"] })
    val b = println(resource { pr["asd4"] })
    val c = println(resource { pr["asd5"] })
//
//    val provider = create {
//        cache(source = cache(hashMapStorage())) { input: String ->
//            File(input).inputStream()
//        }
//    }
//
//    val prov = provider {
//        cache(source = cache(hashMapStorage())) { input: String ->
//            File(input).inputStream()
//        }
//    }
//    val etc = prov["asd"]
//
//    val tes =
//        Provider { input: String -> File(input).inputStream() }
//            .using { input ->
//                Provider(
//                    cached(provider = -input)
//                )
//            }


//    provider { input: String -> File(input).inputStream() } transform { input ->
//        cached(input)
//    }
//    val prov = provider(
//        definition = cached { input: String -> File(input).inputStream() }
//    )

//    val prov = provider ({ input: String ->
//        cached { input: String -> File(input).inputStream() }.invoke(input)
//    })
//    val prov =
//        provider({ input: String -> File(input).inputStream() } cached cache(hashMapStorage()))

//
//    val prov2 = Provider { input: String ->
//        File(input).inputStream()
//    }.asCached()

//    val prov2 = provider { input: String ->
//        File(input).inputStream()
//    }.asCached()
}

//
inline infix fun <I, R> I.using(transformation: (I) -> R): R {
    return transformation(this)
}

inline fun <I, R, O> ((I) -> R).output(
    transformation: ((I) -> R) -> ((I) -> O)
): ((I) -> O) {
    return transformation(this)
}


inline fun <reified I, T> forward(va: T, scope: () -> (T) -> I) = scope().invoke(va)
//
//inline fun <reified T, R> create(transformation: () -> ((T) -> R)): (T) -> R {
//    return transformation()
//}