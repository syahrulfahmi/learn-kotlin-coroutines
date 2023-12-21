import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

@OptIn(DelicateCoroutinesApi::class)
class SequentialSuspendTest {

    private suspend fun getFoo(): Int {
        delay(1_000)
        return 10
    }

    private suspend fun getBar(): Int {
        delay(1_000)
        return 5
    }

    /**
     * by default suspend function itu berjalan secara
     * sequential sama seperti function biasanya, yang
     * membedakan adalah pada suspend function, proses
     * bisa di tangguhkan
     */
    @Test
    fun testSuspend() = runBlocking {
        val time = measureTimeMillis {
            val job = GlobalScope.launch {
                getFoo()
                getBar()
            }
            job.join()
        }
        println("Total Time: $time")
    }

    @Test
    fun testConcurrent() = runBlocking {
        val time = measureTimeMillis {
            val job1 = GlobalScope.launch { getFoo() }
            val job2 = GlobalScope.launch { getBar() }
            joinAll(job1, job2)
        }
        println("Total Time: $time")
    }
}