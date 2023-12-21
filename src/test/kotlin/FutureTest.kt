import org.junit.jupiter.api.Test
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis

class FutureTest {

    private val executorService: ExecutorService = Executors.newFixedThreadPool(3)

    private fun getFoo(): Int {
        Thread.sleep(1_000)
        return 10
    }

    private fun getBar(): Int {
        Thread.sleep(1_000)
        return 10
    }

    @Test
    fun testNonParallel() {
        val timer = measureTimeMillis {
            val foo = getFoo()
            val bar = getBar()
            val sum = foo + bar
            println("total $sum")
        }
        println("Time $timer")
    }

    @Test
    fun testFuture() {
        val timer = measureTimeMillis {
            val foo = executorService.submit(Callable { getFoo() })
            val bar = executorService.submit(Callable { getBar() })
            val total = foo.get() + bar.get()
            println("total: $total")
        }
        println("time: $timer")
    }
}