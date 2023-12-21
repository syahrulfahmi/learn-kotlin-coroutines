import kotlinx.coroutines.*
import org.junit.jupiter.api.Test

@OptIn(DelicateCoroutinesApi::class, ExperimentalStdlibApi::class)
class CoroutineContextTest {

    @Test
    fun testCoroutineContext() = runBlocking {
        val job = GlobalScope.launch {
            val context = coroutineContext
            println(context)
            println(context[Job])
            println(context[CoroutineDispatcher])
        }
        job.join()
    }
}