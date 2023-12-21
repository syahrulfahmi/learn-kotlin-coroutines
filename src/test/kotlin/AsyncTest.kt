import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

@OptIn(DelicateCoroutinesApi::class)
class AsyncTest {

    /**
     * Async mengembalikan object Deferred. Deffered adalah turunan dari job yang membawa
     * hasil dari async function
     */
    private suspend fun getFoo(): Int {
        delay(1_000)
        return 10
    }

    private suspend fun getBar(): Int {
        delay(1_000)
        return 5
    }

    @Test
    fun testSuspendAsync() {
        runBlocking {
            val time = measureTimeMillis {
                val foo = GlobalScope.async { getFoo() }
                val bar = GlobalScope.async { getBar() }
                val total = foo.await() + bar.await()
                println("total sum: $total")
            }

            println("Total Time: $time")
        }
    }

    @Test
    fun testAwaitAll() {
        runBlocking {
            val time = measureTimeMillis {
                val foo = GlobalScope.async { getFoo() }
                val bar = GlobalScope.async { getBar() }
                val total = awaitAll(foo, bar).sum()
                println("total sum: $total")
            }

            println("Total Time: $time")
        }
    }
}