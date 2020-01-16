import com.source.log.log
import com.source.resource.Resource
import com.source.resource.resource
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.net.URL
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.*
import kotlin.coroutines.Continuation
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis


/**
 * This file currently holds ideas of how to use basic bricks
 * */

suspend fun <T> deferred(body: suspend () -> T) = coroutineScope {
    async { body() }.await()
}

suspend fun <T> parallel(strategy: Mutex, body: () -> T): suspend () -> T {
    var result: T? = null
    return {
        strategy.withLock {
            result ?: body().also { result = it }
        }
    }
}


fun <T : Any> singleExecution(body: suspend () -> T): suspend () -> T {
    var completedResult: Result<T>? = null

    val firstExecution = AtomicInteger(0)

    return {
        var counter = 0
        if (firstExecution.getAndIncrement() == 0) {
            "isFirstExecution execution start".log()
            completedResult = runCatching { body() }
        } else {
            "awaiting execution results".log()
            while (completedResult == null) {
                "yielded ${++counter}".log()
                yield()
            }
            "finished awaiting execution".log()
        }

        val result = completedResult
        if (result == null) {
            throw IllegalStateException()
        } else {
            result.getOrThrow()
        }
    }
}

fun <T : Any> singleExecution2(body: suspend () -> T): suspend () -> T {
    val job = Job()
    var completedResult: Result<T>? = null
    var canHandle = false;
    val firstExecution = AtomicInteger(0)

    return suspend {
        var counter = 0
        if (firstExecution.getAndIncrement() == 0) {
            "isFirstExecution execution start".log()
            job.start()
            body.startCoroutine(Continuation(coroutineContext) { result ->
                result.log("#0")
                completedResult = result
                result.onSuccess {
                    it.log("#1")
                    job.complete()
                }
                    .onFailure {
                        it.log("#2")
                        job.completeExceptionally(it)
                    }
                canHandle = true
            })
            "isFirstExecution execution finished".log()
        }

        "awaiting execution results".log()
        job.join()
        "finished awaiting execution".log()

        val result = completedResult
        if (result == null)
            throw IllegalStateException()
        result.getOrThrow()
    }
}

fun <T, R> test(func: suspend (T) -> R): suspend (T) -> R {
    return { input: T -> func(input) }
}


fun main2() {
    val host = "https://jsonplaceholder.typicode.com"


    val mutex = Mutex()
    val job = Job()
    val context = job + Dispatchers.Default
    val scope = CoroutineScope(context)

    val scope2 = hashMapOf<String, Deferred<Resource<ByteArray>>>()
//    val scope2 = hashMapOf<String, suspend () -> Resource<ByteArray>>()

    val inc = AtomicInteger(0)
    val pr = test { input: String ->
        "requesting $input".log()
        coroutineScope {
            scope2[input]?.await() ?: run {
                if (mutex.tryLock(input)) {
                    "locking for $input".log()
                    scope2[input] ?: run {
                        val deferred = async {
                            resource {
                                "start of $input".log()
                                val res = URL("$host/$input").openConnection().getInputStream()
                                    .readBytes()
                                "end of $input".log()
                                inc.incrementAndGet()
                                res
                            }.also {
                                "when completed action $input".log()
                                mutex.withLock(input) {
                                    scope2.remove(input)
                                }
                                "finished when completed action $input".log()
                            }
                        }
                        "unlocking for $input".log()
                        scope2[input] = deferred
                        mutex.unlock(input)
                        deferred
                    }
                } else {
//                    mutex.withLock(input) { scope2[input]!! }
                    var deferred = scope2[input]
                    while (deferred == null) {
                        println("yielding at $coroutineContext")
                        yield()
                        deferred = scope2[input]
                    }
                    deferred
                }
            }.await()
        }.also {
            "end of requesting $input".log()
        }
    }


    data class Result<T>(
        val input: String,
        val time: Long,
        val data: T
    )

    val mutex3 = Mutex()
    val statistics = mutableListOf<Result<Resource<ByteArray>>>()
    suspend fun requestSingle(index: Int, el: Int) {
        val input = "todos/$el"
        lateinit var temp: Resource<ByteArray>
        val time = measureNanoTime {
            temp = pr(input)
        }
        mutex3.withLock {
            statistics.add(
                Result(
                    input = input,
                    time = time,
                    data = temp
                )
            )
        }
    }

    val res =
        scope.launch {
            List(5) {
                (0 until 6).mapIndexed { index, el ->
                    log("launching $el[$index]")
                    launch {
                        requestSingle(index, el)
                    }
                    log("launched $el[$index]")
//                    delay(Random.nextLong() % 100)
                }
            }

            delay(1500)

            List(5) {
                (0 until 31).mapIndexed { index, el ->
                    launch {
                        requestSingle(index, el / 5)
                    }
                }
                delay(500)
            }
        }

    runBlocking {
        while (!res.isCompleted) {
            "${scope2.size}: ${scope2.keys}".log("current jobs")
            delay(50)
        }
    }

    statistics.sortedBy { it.input }.forEach { result ->
        "request: ${result.input} => time: ${result.time}, data: ${result.data}".log("Request")
    }


    val acc = statistics.sumByDouble { it.time.toDouble() } / statistics.size
    val min = statistics.minBy { it.time }?.time ?: 0
    val max = statistics.maxBy { it.time }?.time ?: 0
    "avg time: ${acc.toLong()} [min: $min, max: $max], requests: ${inc.get()}".log()
    "${scope2.size}".log()
    "${scope2.keys.sortedBy { it }}".log()

//
//
//    val requestProvider = provide(
//        context = cache<String, Resource<ByteArray>>(
//            discriminator = { Discriminate.not<Resource.Data<*>>(it.value) }
//        ),
//        init = { input: String ->
//            get("$host/$input")
//        },
//        next = { input, cache ->
//            suspend {
//                cache ?: +resource {
//                    URL("$host/$input").openConnection().getInputStream().readBytes()
//                }.index(input)
//            }
//        }, exit = { input, cached, result ->
//            cached?.log("\trequestProvider from cache [$input]")
//                ?: result.log("\trequestProvider from eval [$input]")
//        }
//    )
//
//    val todoProvider = CacheStore.provider(
//        cache = cache(
//            discriminator = { Discriminate.not<Resource.Data<*>>(it.value) }
//        ), provider = { input: Int ->
//            suspend {
//                println("before semaphore")
////                semaphore.withPermit {
//                println("in semaphore")
//                requestProvider["todos/$input"].invoke().map { bytes -> String(bytes) }
////                }
//                println("after semaphore")
//            }
//        }, exit = { input, cached, result ->
//            cached?.log("\ttodoProvider from cache [$input]")
//                ?: result.log("\ttodoProvider from eval [$input]")
//        }
//    )
//
//    val req = List(3) { it ->
//        todoProvider[it]
//    }.map { it -> listOf(it, it) }.flatten()

//    runBlocking {
//        withContext(context){
//            req.mapIndexed { index, el ->
//                            launch {
//                val time = measureNanoTime {
//                    el.invoke().log("id: $index")
//                }
//                "took: $time ns\n".log()
//            }
//            }//.joinAll()
//        }
//    }
}
