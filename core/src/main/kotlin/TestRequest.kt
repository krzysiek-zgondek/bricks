import com.source.log.log
import com.source.resource.Resource
import com.source.resource.resource
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import org.junit.Assert
import org.junit.Test
import java.net.URL
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/*
*   FileRepository -> request: path => result: File at path
*   NetRepository -> request: path => result: Response from path
*
*
* */

/*
* query -> provider -> dispatch -> request | cache | executor -> result
*                               -> request | call  | executor -> result
*
*
* query -> provider -> request(P)
* request(P) -> dispatch -> request(D)
* request(D) ->
*
* */

val host = "https://jsonplaceholder.typicode.com"

data class Request<T>(val source: () -> T)

private inline infix fun <T, R> Request<T>.by(executor: (T) -> R): R {
    return executor(source())
}

interface Provider<RequestType, ResultType> {
    fun acquire(request: RequestType): ResultType
}

class URLRequestProvider : Provider<String, Resource<URL>> {
    override fun acquire(request: String): Resource<URL> {
        return resource { URL(request) }
    }
}

inline fun <RequestType, ResultType> provider(crossinline executor: (input: RequestType) -> ResultType): Provider<RequestType, ResultType> {
    return object : Provider<RequestType, ResultType> {
        override fun acquire(request: RequestType): ResultType {
            return executor(request)
        }
    }
}

interface ParallelStrategy {
    suspend fun enterScope()
    fun exitScope()
}

class SingleExecution : ParallelStrategy {
    val mutex: Mutex = Mutex()

    override suspend fun enterScope() {
        mutex.lock()
    }

    override fun exitScope() {
        mutex.unlock()
    }
}

class LimitedExecution(limit: Int) : ParallelStrategy {
    val semaphore: Semaphore = Semaphore(limit)

    override suspend fun enterScope() {
        semaphore.acquire()
    }

    override fun exitScope() {
        semaphore.release()
    }
}


suspend inline fun <T> withScope(strategy: ParallelStrategy, block: () -> T): T {
    strategy.enterScope()
    try {
        return block()
    } finally {
        strategy.exitScope()
    }
}

suspend inline fun <T> ParallelStrategy.coSingle(block: () -> T): T {
    enterScope()
    try {
        return block()
    } finally {
        exitScope()
    }
}

suspend inline fun <T> parallel(strategy: ParallelStrategy, block: ParallelStrategy.() -> T): T {
    return block(strategy)
}

inline fun <K, V> withCache(storage: HashMap<K, V>, key: K, provider: () -> V): V {
    return storage[key]?.also { "$key from Cache".log() } ?: run {
        provider().also { value -> storage[key] = value }
    }.also { "$key from Provider".log() }
}


inline fun <T> CompletableDeferred<T>.completeWith(result: Result<T>) {
    result.onSuccess { value ->
        complete(value)
    }.onFailure { error ->
        completeExceptionally(error)
    }
}


class Requester {
    enum class RequestState {
        Initialized,
        Started,
    }

    private val state = atomic<Any?>(RequestState.Initialized)

    suspend fun <T> execute(request: suspend () -> T): T {
        val shouldExecute = state.compareAndSet(RequestState.Initialized, RequestState.Started)
        if (shouldExecute) {
            return runCatching { request() }.let { result ->
                state.value = result
                result.getOrThrow()
            }
        } else {
            while (state.value === RequestState.Started)
                yield()
            val result = state.value as Result<T>
            return result.getOrThrow()
        }
    }
}

class RequestExecutor(val cache: HashMap<Any, Requester> = hashMapOf()) :
    AbstractCoroutineContextElement(RequestExecutor) {
    companion object Key : CoroutineContext.Key<RequestExecutor>
}



class TodoRepository {
    private val scope = Dispatchers.IO + RequestExecutor()
    private val strategy = SingleExecution()

    private fun formPath(id: Int) = "$host/todos/$id"

    suspend fun todos(id: Int): Resource<String> = withContext(scope) {
        val executor = coroutineContext[RequestExecutor] ?: throw IllegalStateException("")
        val cache = executor.cache

        val requester = withScope(strategy) {
            cache.getOrPut(formPath(id)) { Requester() }
        }

        requester.execute {
            resource {
                "launching $id".log()
                String(
                    URL("$host/todos/$id").openConnection().getInputStream().readBytes()
                )
            }
        }
    }
}

fun drawResults(result: Pair<List<Long>, List<Long>>) {
    val (warmUp, benchmark) = result
    benchmark.minBy { it }?.log("min")
    benchmark.maxBy { it }?.log("max")
    benchmark.average().log("avg")
}

class `Test Deferred vs CompletableDeferred create time` {
    @Test
    fun `test async create time`() {
        println("async")
        benchmark(warmUp = 100, repeat = 1000) {
            calculateAsyncStep()
        }.also { drawResults(it) }
    }


    @Test
    fun `test completable deferred create time`() {
        println("CDL")
        benchmark(warmUp = 100, repeat = 1000) {
            calculateCDLStep()
        }.also { drawResults(it) }
    }

    @Test
    fun `test custom completable-job create time`() {
        println("CCDL")
        benchmark(warmUp = 100, repeat = 1000) {
            calculateCCDLStep()
        }.also { drawResults(it) }
    }
}

//this test is prone to fail even if everything is allright
//has to be refactored but is good enough for now
class `Strategy Execution` {
    @Test
    fun `test single execution`() {
        val strategy = SingleExecution()
        var counter = 0
        runBlocking {
            repeat(1000) {
                launch {
                    parallel(strategy) {
                        counter++;
                    }
                }
            }
        }
        Assert.assertEquals(1000, counter)

        runBlocking {
            repeat(1000) {
                launch {
                    counter++
                }
            }
        }

        Assert.assertNotEquals(1000, counter)
    }
}

class `Request data class` {

    @Test
    fun `donno`() {
        val repo = TodoRepository()

        runBlocking {
            repeat(16) {
                launch {
                    repo.todos(it % 4).log("finished ${it / 4}")
                }.log("launched ${it % 4} repeat ${it / 4}")
            }
        }.log()
    }

    @Test
    fun `test url equality`() {
        val urls = listOf(
            URL("$host/todos/1"),
            URL("$host/todos/2"),
            URL("$host/todos/3")
        )

        urls.forEachIndexed { index, first ->
            Assert.assertTrue(
                urls.drop(index + 1).none { second -> second == first }
            )
        }

        val containsDuplicate = urls.take(0) + urls
        containsDuplicate.forEachIndexed { index, first ->
            Assert.assertFalse(
                urls.none { second -> second == first }
            )
        }
    }

    @Test
    fun `test request equality`() {
        val requests = listOf<Any>(
            Request { "I'm a string" },
            Request { "I'm a second string" },
            Request { 100 },
            Request { "I'm a string" }
        )

        println(Request { "I'm a string" } === Request { "I'm a string" })

        requests.forEachIndexed { index, first ->
            Assert.assertTrue(
                requests.drop(index + 1).none { second -> second == first }
            )
        }
    }
}