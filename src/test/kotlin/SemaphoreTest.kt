import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

class SemaphoreTest {

    /**
     * Secara konsep mirip dengan mutex, hanya yang membedakan pada mutex hanya bisa diakses pada satu coroutine,
     * sedangkan semaphore bisa kita tentukan berapa thread yang bisa berjalana pada satu waktu.
     */

    @Test
    fun testSemaphore() {
        var counter = 0
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)
        val semaphore = Semaphore(2)
        repeat(100) {
            scope.launch {
                repeat(1000) {
                    semaphore.withPermit {
                        counter++
                    }
                }
            }
        }
        runBlocking {
            delay(2000)
            println("Total: $counter")
        }
    }
}