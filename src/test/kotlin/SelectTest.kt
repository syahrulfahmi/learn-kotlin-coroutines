import kotlinx.coroutines.*
import kotlinx.coroutines.selects.select
import org.junit.jupiter.api.Test

class SelectTest {
    /**
     * select funct merupakan sebuah fungsi yang memungkinkan kita untuk menunggu beberapa suspend func
     * dan memilih yang pertama datanya tersedia.
     *
     * select funct bisa digunakan di Deferred atau juga channel
     *
     * untuk Deferred, kita bisa gunakan onAwait()
     * dan untuk ReceiveChannel, kita bisa gunakan onReceive()
     */

    @Test
    fun testSelectDeferred() {
        runBlocking {
            val scope = CoroutineScope(Dispatchers.IO)
            val deferred1 = scope.async { delay(1000); 1000 }
            val deferred2 = scope.async { delay(2000); 2000 }
            val win = select<Int> {
                deferred1.onAwait { it }
                deferred2.onAwait { it }
            }
            println("win $win")
        }
    }
}