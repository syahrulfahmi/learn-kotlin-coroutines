import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.Date

@OptIn(DelicateCoroutinesApi::class)
class TimeoutTest {

    @Test
    fun testWithTimeout() = runBlocking {
        val job = GlobalScope.launch {
            withTimeout(5_000) {
                repeat(100) {
                    delay(1_000)
                    println("$it Start Coroutine: ${Date()}")
                }
            }
            println("Finish Coroutine")
        }
        job.join()
    }

    @Test
    fun testWithTimeoutOrNull() = runBlocking {
        val job = GlobalScope.launch {
            withTimeoutOrNull(5_000) {
                repeat(100) {
                    delay(1_000)
                    println("$it Start Coroutine: ${Date()}")
                }
            }
            println("Finish Coroutine")
        }
        job.join()
    }
}