import com.source.cache.cache
import com.source.cache.provider
import com.source.indexable.indexedvalue.index
import com.source.provider.*
import com.source.provider.get
import com.source.resource.Resource
import com.source.resource.resource
import com.source.storage.map.hashMapStorage
import java.io.File
import java.io.FileOutputStream

/**
 * This file currently holds ideas of how to use basic bricks*/

fun main() {
    val cache = provider(
        cache = cache(
            storage = hashMapStorage(),
            discriminator = { entry ->
                entry.value !is Resource.Data
            }
        ),
        provider = { input: String ->
            resource { File(input).outputStream() }
        }
    )


    val prov = provider(
        context = cache(
            storage = hashMapStorage<String, Resource<FileOutputStream>>(),
            discriminator = { entry -> entry !is Resource.Data<*> }
        ),
        init = { input: String -> get(input) },
        next = { input, last ->
            last ?: +resource { File(input).outputStream() }.index(input)
        }
    )

    val item = cache["asd"]
    val item1 = cache["asd"]
    val item2 = prov["asd"]
    val item3 = prov["asd"]
}