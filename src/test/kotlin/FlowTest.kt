import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.jupiter.api.Test

class FlowTest {

    /**
     * flow mirip dengan sequence, yang membedakan adalah flow berjalan sebagai coroutine
     * dan kita bisa menggunakan suspend func di flow.
     *
     * Flow adalah collection cold atau lazy, artinya jika tidak diminta datanya, flow tidak akan berjalan.
     *
     * emit() : mengirim data
     * collect() : mengakses data pada flow
     */

    @Test
    fun testFlow() {
        val flow = flow {
            println("start flow")
            repeat(10) {
                emit(it)
            }
        }
        runBlocking {
            flow.collect {
                println(it)
            }
        }
    }

    suspend fun numberFlow(): Flow<Int> = flow {
        repeat(100) {
            emit(it)
        }
    }

    suspend fun changeToString(number: Int): String {
        delay(100)
        return "Number :  $number"
    }

    @Test
    fun testFlowOperator() {
        runBlocking {
            val flow1 = numberFlow()
            flow1.filter { it % 2 == 0 }.map { changeToString(it) }.collect { println(it) }
        }
    }

    @Test
    fun testFlowException() {
        runBlocking {
            val flow = numberFlow()
            flow.map { check(it < 10); it }.onEach { println(it) } //
                .catch { println("Error ${it.message}") } // menangkap error
                .onCompletion { println("Done") } // sama seperti blok finnaly
                .collect()
        }
    }

    /**
     * untuk membatalkan flow, kita bisa gunakan cancel()
     */
    @Test
    fun testFlowCancellable() {
        runBlocking {
            val flow = numberFlow()
            val scope = CoroutineScope(Dispatchers.IO)
            val job = scope.launch {
                flow.onEach {
                    if (it > 10) this.cancel()
                    else println("Number $it in ${Thread.currentThread().name}")
                }.collect()
            }
            job.join()

        }
    }
}