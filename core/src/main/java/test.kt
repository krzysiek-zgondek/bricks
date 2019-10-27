import com.source.cache.cache
import com.source.core.Source
import com.source.provider.Provider
import com.source.resource.Resource
import com.source.resource.resource
import com.source.storage.map.hashMapStorage
import java.io.File
import java.io.FileInputStream

typealias Dispatcher<Item, Result> = (Item) -> Result

inline infix fun <T, R> T.from(dispatcher: (T) -> R): R {
    return dispatcher(this)
}

//fun test1() {
//    val cache = cache(
//        storage = hashMapStorage<String, FileInputStream>()
//    )
//
//    val provider = Provider { input: String ->
//        resource {
//            cache.getOrObtain(input) { id -> File(id).inputStream() }
//        }
//    }
//
//    val request = Source { provider["name"] }
//    when (val resResult = request.get()) {
//        is Resource.Data -> resResult.value
//        is Resource.Error -> resResult.value
//    }
//}
//
//fun test2() {
//    val cache = cache(
//        storage = hashMapStorage<String, FileInputStream>()
//    )
//
//    val provider = Provider { input: String ->
//        cache.getOrObtain(input) { id -> File(id).inputStream() }
//    }
//
//    when (val result = resource { provider["name"] }) {
//        is Resource.Data -> result.value
//        is Resource.Error -> result.value
//    }
//}
//
//fun test3() {
//    val cache = cache(
//        storage = hashMapStorage<String, FileInputStream>()
//    )
//
//    val provider = Provider { input: String ->
//        cache.getOrObtain(input) { id -> File(id).inputStream() }
//    }
//
//    when (val result = resource { provider["name"] }) {
//        is Resource.Data -> result.value
//        is Resource.Error -> result.value
//    }
//}