import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

@OptIn(DelicateCoroutinesApi::class)
class JobTest {

    @Test
    fun testJob() = runBlocking {
        val job = GlobalScope.launch(start = CoroutineStart.LAZY) {
            delay(2_000)
            println("Coroutine Done: ${Thread.currentThread().name}")
        }

        job.start()
        delay(3_000)
    }

    @Test
    fun testJobJoin() = runBlocking {
        val job = GlobalScope.launch {
            ensureActive()
            delay(2_000)
            println("Coroutine Done: ${Thread.currentThread().name}")
        }

        job.join()
    }

    @Test
    fun testJobCancel() = runBlocking {
        val job = GlobalScope.launch {
            delay(2_000)
            println("Coroutine Done: ${Thread.currentThread().name}")
        }

        job.cancel() // expect output tidak keluar karena job sudah di cancel
        delay(3_000)
    }


    /**
     * secara default jika suatu job mengalami error, maka coroutine akan mengekskalasikan
     * error tersebut ke parent dan akan membatalkan semua coroutine yang berjalan di parent tersebut.
     */
    @Test
    fun testJob2() {
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(Job() + dispatcher)
        val job1 = scope.launch {
            delay(2000)
            println("Job 1 Complete")
        }
        val job2 = scope.launch {
            delay(1000)
            throw IllegalArgumentException("Job 2 failed")
        }
        runBlocking {
            joinAll(job1, job2)
        }
    }

    /**
     * supervisor job adalah cara untuk membuat setiap coroutine memiliki independensi tersendiri
     * dalam menangani error, artinya jika suatu job mengalami error, maka parent tidak akan membatalkan
     * coroutine yang lain.
     */
    @Test
    fun testSupervisorJob() {
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(SupervisorJob() + dispatcher)
        val job1 = scope.launch {
            delay(2000)
            println("Job 1 Complete")
        }
        val job2 = scope.launch {
            delay(1000)
            throw IllegalArgumentException("Job 2 failed")
        }
        runBlocking {
            joinAll(job1, job2)
        }
    }

    @Test
    fun testSupervisorScope() {
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(Job() + dispatcher)
        runBlocking {
            scope.launch {
                supervisorScope {
                    launch {
                        delay(2000)
                        println("child 1 done")
                    }
                    launch {
                        delay(1000)
                        throw IllegalArgumentException("child 2 error")
                    }
                }
            }
            delay(3000)
        }
    }
}