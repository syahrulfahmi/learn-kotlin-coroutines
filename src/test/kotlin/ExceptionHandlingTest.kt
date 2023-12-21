import kotlinx.coroutines.*
import org.junit.jupiter.api.Test

class ExceptionHandlingTest {

    @Test
    fun testExceptionLaunch() {
        runBlocking {
            val job = launch {
                println("Start Coroutine")
                throw IllegalArgumentException()
            }

            job.join()
            println("Finish")
        }
    }

    @Test
    fun testExceptionAsync() {
        runBlocking {
            val deferred = async {
                println("Start coroutine")
                throw IllegalArgumentException()
            }
            try {
                deferred.await()
            } catch (error: IllegalArgumentException) {
                println("Error")
            } finally {
                println("Finish")
            }
        }
    }

    @Test
    fun testExceptionHandler() {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            println("Ups, error with message ${throwable.message.orEmpty()}")
        }
        val scope = CoroutineScope(exceptionHandler + Dispatchers.IO)
        val job = scope.launch {
            println("Start Job")
            throw IllegalArgumentException("ups")
        }
        runBlocking {
            job.join()
        }
    }

    /**
     * secara default exception handling hanya boleh di lakukan di parent dan tidak boleh
     * dilakukan pada child. untuk bisa melakukan exception handling di child, kita bisa gunakan
     * supervisor scope.
     */
    @Test
    fun testJobExceptionHandler() {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            println("Ups, error with message ${throwable.message.orEmpty()}")
        }
        val scope = CoroutineScope(Dispatchers.IO)
        runBlocking {
            val job = scope.launch {
                launch(exceptionHandler) { // not used cause cannot handling exception in child

                }
            }
            job.join()
        }
    }

    @Test
    fun testSupervisorJobExceptionHandler() {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            println("Ups, error with message ${throwable.message.orEmpty()}")
        }
        val scope = CoroutineScope(Dispatchers.IO)
        runBlocking {
            val job = scope.launch {
                supervisorScope {
                    launch(exceptionHandler) {
                        launch() {
                            println("Job child")
                            throw IllegalArgumentException("child error")
                        }
                    }
                }
            }
            job.join()
        }
    }
}