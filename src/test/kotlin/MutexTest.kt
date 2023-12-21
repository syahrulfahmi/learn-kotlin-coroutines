import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

class MutexTest {

    /**
     * Mutex atau Mutual Exclusion adalah salah satu fitur di coroutine yang berfungsi untuk
     * melakukan locking atau penguncian. Dengan menggunakan mutex, kita bisa pastikan bahwa hanya ada
     * 1 coroutine yang bisa mengakses kode tersebut.
     */

    @Test
    fun testMutex() {
        var counter = 0
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)
        val mutext = Mutex()
        repeat(100) {
            scope.launch {
                repeat(1000) {
                    mutext.withLock {
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