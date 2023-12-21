import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.junit.jupiter.api.Test

class SharedFlowTest {

    @Test
    fun testSharedFlow() {
        val sharedFlow = MutableSharedFlow<Int>(10)
        val scope = CoroutineScope(Dispatchers.IO)
        runBlocking {
            scope.launch { repeat(10) { delay(1000); sharedFlow.emit(it) } }
            scope.launch { sharedFlow.asSharedFlow().collect { delay(2000); println("Shared Flow 1 receive $it") } }
            scope.launch { sharedFlow.asSharedFlow().collect { delay(100); println("Shared Flow 2 receive $it") } }
            delay(21_000)
            scope.cancel()
        }
    }
}