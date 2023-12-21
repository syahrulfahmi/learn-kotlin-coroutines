import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

class CoroutineScopeTest {

    /**
     * Coroutine Scope merupakan object lifecycle dari coroutine
     * CoroutineScope biasanya digunakan pada flow yang saling berhububungan,
     * Contoh, akses server data dan menampilkannya pada aplikasi mobile
     *
     * Bagaimana dengan GlobalScope?
     * Penggunaaan GlobalScope tidak dianjurkan pada pembuatan aplikasi, karena jika
     * semua coroutine menggunakan GlobalScope, maka secara otomatis akan sharing coroutine scope.
     * Hal ini agak menyulitkan saat kita ingin membatalkan sebuah flow, karena saat sebuah coroutine
     * scope dibatalkan, maka semua coroutine yang terdapat pada scope tersebut akan dibatalkan.
     */

    @Test
    fun testCoroutineScope() {
        runBlocking {
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                delay(1000)
                println("Start ${Thread.currentThread().name}")
            }
            scope.launch {
                delay(1000)
                println("Start ${Thread.currentThread().name}")
            }
            delay(2000)
            println("DONE")
        }
    }

    @Test
    fun testCoroutineScopeCancel() {
        runBlocking {
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                delay(2000)
                println("Start ${Thread.currentThread().name}")
            }
            scope.launch {
                delay(2000)
                println("Start ${Thread.currentThread().name}")
            }
            delay(1000)
            scope.cancel()
            delay(2000)
            println("DONE")
        }
    }

    private suspend fun getFoo(): Int {
        delay(1_000)
        return 10
    }

    private suspend fun getBar(): Int {
        delay(1_000)
        return 5
    }

    private suspend fun getSum(): Int = coroutineScope {
        val foo = async { getFoo() }
        val bar = async { getBar() }
        foo.await() + bar.await()
    }

    @Test
    fun testCoroutineScopeFunction() {
        runBlocking {
            val scope = CoroutineScope(Dispatchers.IO)
            val job = scope.launch {
                val result = getSum()
                println("Total is: $result")
            }
            job.join()
        }
    }

    @Test
    fun testParentChildDispatcher() = runBlocking {
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)
        val job = scope.launch {
            println("Parent scope ${Thread.currentThread().name}")
            coroutineScope {
                launch {
                    println("Child scope: ${Thread.currentThread().name}")
                }
            }
        }
        job.join()
    }

    @Test
    fun testParentChildDispatcherCancel() = runBlocking {
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)
        val job = scope.launch {
            println("Parent scope ${Thread.currentThread().name}")
            coroutineScope {
                launch {
                    delay(2000)
                    println("Child scope: ${Thread.currentThread().name}")
                }
            }
        }
        job.cancelAndJoin()
    }

    @Test
    fun testCancelChildren() = runBlocking {
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)
        val job = scope.launch {
            coroutineScope {
                launch {
                    delay(2000)
                    println("Child scope: ${Thread.currentThread().name}")
                }
                launch {
                    delay(3000)
                    println("Child 2 : ${Thread.currentThread().name}")
                }
                println("Parent scope ${Thread.currentThread().name}")
            }
        }
        job.cancelAndJoin()
        println("DONE")
    }

    @Test
    fun testCoroutineName() = runBlocking {
        val job = CoroutineScope(Dispatchers.IO + CoroutineName("parent")).launch {
            println("Run in thread: ${Thread.currentThread().name}")
            withContext(CoroutineName("child")) {
                println("Run in thread: ${Thread.currentThread().name}")
            }
        }
        job.join()
    }
}