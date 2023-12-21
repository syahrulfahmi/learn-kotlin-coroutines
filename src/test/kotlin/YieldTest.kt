import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

class YieldTest {
    /**
     * Coroutine berjalan secara concurrent. 1 Dispatcher bisa digunakan
     * beberapa coroutine secara bergantian. Saat coroutine berjalan dan ingin
     * memberikan kesempatan ke coroutine lain untuk berjalan, kita bisa gunakan @yield function.
     *
     * Yield function bisa di cancel, artinya jika sebuah coroutine dibatalkan, maka
     * otomatis yield function akan throw CancellationException.
     */

    private suspend fun runJob(number: Int) {
        println("Start job $number in ${Thread.currentThread().name}")
        yield()
        println("Start job $number in ${Thread.currentThread().name}")
    }

    @Test
    fun testYieldFunction() {
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)
        runBlocking {
            scope.launch { runJob(1) }
            scope.launch { runJob(2) }
            delay(2000)
        }
    }
}
