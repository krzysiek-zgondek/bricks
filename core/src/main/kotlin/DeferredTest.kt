import kotlinx.coroutines.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.startCoroutine
import kotlin.system.measureNanoTime

/**
 * @project Bricks
 * @author SourceOne on 28.11.2019
 */

/*I know that there are better ways to benchmark speed
* but in this case this method is fine enough
* given the produced results*/
fun benchmark(warmUp: Int, repeat: Int, action: suspend () -> Unit): Pair<List<Long>, List<Long>> {
    val warmUpResults = List(warmUp) {
        measureNanoTime {
            runBlocking {
                action()
            }
        }
    }

    val benchmarkResults = List(repeat) {
        measureNanoTime {
            runBlocking {
                action()
            }
        }
    }

    return warmUpResults to benchmarkResults
}


/* find way to cancel startedCoroutine when deferred is
*  canceled (currently you have to cancel whole context)
* */
fun <T> CoroutineScope.coAsync(provider: suspend () -> T): Deferred<T> {
    return CompletableDeferred<T>().also { completable ->
        provider.startCoroutine(
            Continuation<T>(coroutineContext) { result ->
                completable.completeWith(result)
            }
        )
    }
}

suspend fun calculateAsyncStep() = coroutineScope {
    val list = List(10000) {
        async { "i'm a robot" }
    }

    awaitAll(*list.toTypedArray())
}


suspend fun calculateCDLStep() = coroutineScope {
    val list = List(10000) {
        CompletableDeferred<String>().also {
            launch {
                it.complete("i'm a robot")
            }
        }
    }

    awaitAll(*list.toTypedArray())
}


suspend fun calculateCCDLStep() = coroutineScope {
    val list = List(10000) {
        coAsync { "i'm a robot" }
    }

    awaitAll(*list.toTypedArray())
}


//fun main() {
//    runBlocking {
//        val jobs = listOf(
//            launch {
//                val result2 = coAsync { println("delaying result2 #1"); delay(1000); "secondary result #1" }
//                val result = coAsync { println("delaying result #1"); delay(500); cancel(); println("cancelled #1"); "primary result #1" }
//
////                launch {
////                    result.cancel()
////                }
//
//                awaitAll(result2, result)
//            }
////            ,            launch {
////                val result2 = coAsync { println("delaying result2 #2"); delay(1000); "secondary result #2" }
////                val result = coAsync { println("delaying result #2"); delay(1000) }
////
////                launch {
////                    cancel()
////                }
////
////                cancel()
////                awaitAll(result2, result)
////            }
//        )
//
//        jobs.joinAll()
//        "finishing runBlocking"
//    }.log("from runBlocking coAsync")
//    println()
//    runBlocking {
//        val jobs = listOf(
//            launch {
//                val result2 = async(start = CoroutineStart.ATOMIC) { println("delaying result2"); delay(1000); "secondary result" }
//                val result = async(start = CoroutineStart.ATOMIC) { println("delaying result #1"); delay(500); cancel(); println("cancelled #1"); "primary result #1" }
//
//                awaitAll(result2, result)
//            }
////            ,
////            launch {
////                val result2 = async(start = CoroutineStart.ATOMIC) { println("delaying result2"); delay(1000); "secondary result" }
////                val result = async(start = CoroutineStart.ATOMIC) { println("delaying result"); delay(1000) }
////
////                cancel()
////                awaitAll(result2, result)
////            }
//        )
//
//        jobs.joinAll()
//        "finishing runBlocking"
//    }.log("from runBlocking async")
//
////    val labels = listOf("async", "cdl", "ccdl")
////    val collectedResults = listOf(
////        mutableListOf<Pair<List<Long>, List<Long>>>(),
////        mutableListOf(),
////        mutableListOf()
////    )
////
////    "stabilizing runs".log()
////    repeat(5) {
////        println("async $it")
////        benchmark(warmUp = 25, repeat = 200) {
////            calculateAsyncStep()
////        }
////
////        println("CDL $it")
////        benchmark(warmUp = 25, repeat = 200) {
////            calculateCDLStep()
////        }
////
////        println("CCDL $it")
////        benchmark(warmUp = 25, repeat = 200) {
////            calculateCCDLStep()
////        }
////    }
////
////    "\n#Benchmark start".log()
////    val benchmarkTime = measureTimeMillis {
////        repeat(1000) {
////            println("async $it")
////            collectedResults[0] += benchmark(warmUp = 25, repeat = 200) {
////                calculateAsyncStep()
////            }
////
////            println("CDL $it")
////            collectedResults[1] += benchmark(warmUp = 25, repeat = 200) {
////                calculateCDLStep()
////            }
////
////            println("CCDL $it")
////            collectedResults[2] += benchmark(warmUp = 25, repeat = 200) {
////                calculateCCDLStep()
////            }
////        }
////    }
////
////    "\n#Benchmark completed in ${benchmarkTime}ms".log()
////    "#Benchmark results:".log()
////
////    val minMaxAvg = collectedResults.map { stageResults ->
////        stageResults.map { (_, benchmark) ->
////            arrayOf(
////                benchmark.minBy { it }!!, benchmark.maxBy { it }!!, benchmark.average().toLong()
////            )
////        }
////    }
////
////    minMaxAvg.forEachIndexed { index, list ->
////        "results for: ${labels[index]} [min, max, avg]".log()
////        list.forEach { results ->
////            "${results[0]}\t${results[1]}\t${results[2]}".log()
////        }
////    }
//}
fun main() = runBlocking {
    async {
        try {
            delay(1000)
            println("Coroutine done")
        } catch (e: CancellationException) {
            println("Coroutine cancelled, the exception is: $e")
        }
    }
    launch { }
    delay(10)
}

fun CoroutineScope.bareLaunch(block: suspend () -> Unit) =
    block.startCoroutine(Continuation(coroutineContext) { Unit })

fun <T> CoroutineScope.bareAsync(block: suspend () -> T) =
    CompletableDeferred<T>().also { deferred ->
        block.startCoroutine(Continuation(coroutineContext) { result ->
            result.exceptionOrNull()?.also {
                deferred.completeExceptionally(it)
            } ?: run {
                deferred.complete(result.getOrThrow())
            }
        })
    }