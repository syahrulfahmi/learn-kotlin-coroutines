import kotlinx.coroutines.*
import org.junit.jupiter.api.Test

@OptIn(DelicateCoroutinesApi::class)
class CancellableCoroutineTest {

    @Test
    fun testCancellableFinally() = runBlocking {
        val job = GlobalScope.launch {
            try {
                println("start coroutine")
                delay(2_000)
                println("end coroutine")
            } finally {
                println("Finish")
            }
        }
        job.cancelAndJoin()
    }
}