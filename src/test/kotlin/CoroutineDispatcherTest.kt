import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

@OptIn(DelicateCoroutinesApi::class)
class CoroutineDispatcherTest {

    /**
     * CoroutineDispaatcher berguna untuk menentukan thread mana yang
     * bertanggung jawab untuk mengeksekusi coroutine
     */

    /**
     * Dispatcher.Default : isinya minimal 2 thread atau sebanyak jumlah cpu (mana yang lebih banyak).
     * Dispatcher ini cocok untuk proses cpu-bound
     */

    /**
     * Dispatcher.IO : isinya berisikan thread sesuai kebutuhan. Ketika butuh
     * akan dibuat, ketika sudah tidak dibutuhkan akan dihapus, mirip cache thread pool di executor
     */

    /**
     * Dispatcher.Main : ini adalah dispatcher yang berisikan main thread UI, cocok ketika butuh
     * running di thread main seperti JavaFX atau Android. Untuk menggunakan ini biasanya kita
     * perlu menambahkan library ui tambahan.
     */

    /**
     * Dispatcher.Unconfined : dispatcher yang tidak menunjuk thread manapun, biasanya akan
     * melanjutkan thread di coroutine sebelumnya
     */

    /**
     * Dispatcher.Confined (tanpa parameter) :  dispatcher yang akan melanjutkan thread dari coroutine sebelumnya
     *
     * Perbedaan mendasar dari Unconfined dan Confined adalah pada unconfined thread bisa berubah ditengah jalan
     * jika memang terdapat code yang melakukan perubahan thread
     */

    @Test
    fun testDispatcher() {
        println("Unit test in thread ${Thread.currentThread().name}")
        runBlocking {
            val job1 = GlobalScope.launch(Dispatchers.Default) {
                println("Job1 test in thread ${Thread.currentThread().name}")
            }
            val job2 = GlobalScope.launch(Dispatchers.IO) {
                println("Job2 test in thread ${Thread.currentThread().name}")
            }
            joinAll(job1, job2)
        }
    }

    @Test
    fun testRunBlocking() {
        runBlocking {
            GlobalScope.launch(Dispatchers.Unconfined) {
                println("Unconfined: in thread ${Thread.currentThread().name}")
                delay(1_000)
                println("Unconfined: in threa ${Thread.currentThread().name}")
            }
            GlobalScope.launch {
                println("Confined: in thread ${Thread.currentThread().name}")
                delay(1_000)
                println("Confined: in thread ${Thread.currentThread().name}")
            }
            delay(3_000)
        }
    }

    @Test
    fun testExecutorServiceDispatcher() {
        val dispatcherService = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val dispatcherWeb = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        runBlocking {
            val job1 = GlobalScope.launch(dispatcherService) {
                println("Job1 : ${Thread.currentThread().name}")
            }
            val job2 = GlobalScope.launch(dispatcherWeb) {
                println("Job2 : ${Thread.currentThread().name}")
            }
            joinAll(job1, job2)
        }
    }
}