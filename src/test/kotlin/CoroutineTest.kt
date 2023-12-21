import kotlinx.coroutines.*
import org.junit.jupiter.api.Test

@OptIn(DelicateCoroutinesApi::class)
class CoroutineTest {

    private suspend fun hello() {
        delay(1_000)
        println("Hello world")
    }

    @Test
    fun testCoroutine() {
        GlobalScope.launch {
            hello()
        }
        println("MENUNGGU")
        runBlocking {
            delay(1500)
        }
        println("SELESAI")
    }

    @Test
    fun testAwaitCancellation() = runBlocking {
        val job = launch {
            try {
                println("Job started")
            } finally {
                println("Cancelled")
            }
        }

        delay(5000)
        job.cancelAndJoin()
    }
}