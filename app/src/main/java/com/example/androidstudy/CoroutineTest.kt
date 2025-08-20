import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    val n = 50000
    val runtime = Runtime.getRuntime()
    val startMemory = runtime.totalMemory() - runtime.freeMemory()

    val time = measureTimeMillis {
        val jobs = List(n) {
            launch(Dispatchers.Default) {
                var sum = 0
                for (j in 0 until 10) sum += j
            }
        }
        jobs.forEach { it.join() }
    }

    val endMemory = runtime.totalMemory() - runtime.freeMemory()

    println("Coroutine Elapsed Time: $time ms")
    println("Memory Usage: ${(endMemory - startMemory) / 1024} KB")
}
