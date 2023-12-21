import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.map
import org.junit.jupiter.api.Test
import java.util.Date

class StateFlowTest {
    /**
     * state flow adalah turunan dari shareed flow, artinya kita bisa membuat banyak receiver
     *
     * pada state flow, receiver hanya akan menerima data paling baru. Jadi jika ada receiver yang lambat dan sender
     * mengirim data terlalu cepat, yang akan diterima receiver adalah data paling akhir.
     *
     * state flow cocok digunakan untuk mengatur state, dimana memang biasanya state itu biasanya hanya satu data, tidak peduli
     * berapa kali perubahan data tersebut, yang terpenting pada state adalah data paling akhir.
     *
     * untuk mendapatkan data state nya, kita bisa gunakan kata kunci field value di state flow
     *
     * untuk membuat receiver kita bisa gunakan asStateFlow()
     *
     * state flow juga bisa dirancang sebagai pengganti Conflated Broadcast Channel
     */

    @Test
    fun testStateFlow() {
        val stateFlow = MutableStateFlow(0)
        val scope = CoroutineScope(Dispatchers.IO)
        runBlocking {
            scope.launch {
                repeat(10) {
                    println("State Flow Send $it || ${Date()}")
                    stateFlow.emit(it)
                    delay(1000)
                }
            }

            scope.launch {
                stateFlow.asStateFlow()
                    .map { "Receive $it || ${Date()}" }
                    .collect {
                        delay(2000)
                        println(it)
                    }
            }
            delay(20000)
            scope.cancel()
        }
    }
}