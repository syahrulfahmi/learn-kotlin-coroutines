import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

@OptIn(DelicateCoroutinesApi::class)
class WithContextTest {

    @Test
    fun testWithContext() {
        val dispatcherService = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        runBlocking {
            val job = GlobalScope.launch(Dispatchers.IO) {
                println("1 ${Thread.currentThread().name}")
                withContext(dispatcherService) {
                    println("2 ${Thread.currentThread().name}")
                }
                println("3 ${Thread.currentThread().name}")
            }
            job.join()
        }
    }

    @Test
    fun testCancelFinally() {
        runBlocking {
            val job = GlobalScope.launch {
                try {
                    println("Start job")
                    delay(1_000)
                    println("End Job")
                } finally {
                    withContext(NonCancellable) {
                        println(isActive)
                        delay(1_000)
                        println("finally")
                    }
                }
            }
            job.cancelAndJoin()
        }
    }
}