import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChannelTest {
    /**
     * Channel merupakan fitur di coroutine yang berfungsi untuk mengirim aliran data dari
     * satu tempat ke tempat yang lain.
     *
     * channel mirip seperti queue, dimana ada data masuk dan data keluar.
     *
     * untuk mengirim data ke channel, kita bisa gunakan func send()
     * untuk menerima data di channel kita bisa gunakan func receive()
     *
     * channel bersifat blocking artinya jika tidak ada data di channel, saat kita mengambil data
     * menggunakan receive(), maka dia akan menunggu sampai ada datanya. Dan ketika ada datanya di channel
     * dan tidak ada yang mengambilnya saat kita send(), maka dia akan menunggu sampai channel kosong (datanya diambil)
     *
     * untuk menutup channel kita bisa gunakan func.
     */

    @Test
    fun testChannel() {
        /**
         * konsep antrianya, satu kali ngirim menunggu data diterima baru melanjutkan ke
         * antrian yg lain.
         */
        runBlocking {
            val channel = Channel<Int>()
            val job2 = launch {
                println("Send 1")
                channel.send(1)
                println("Send 2")
                channel.send(2)
            }

            val job1 = launch {
                println("Receive ${channel.receive()}")
                println("Receive ${channel.receive()}")
            }

            joinAll(job1, job2)
            channel.close()
        }
    }

    @Test
    fun testProduce() {
        /**
         * konsep antrianya, satu kali ngirim menunggu data diterima baru melanjutkan ke
         * antrian yg lain.
         */
        runBlocking {
//            val channel = Channel<Int>()
//            val job2 = launch {
//                println("Send 1")
//                channel.send(1)
//                println("Send 2")
//                channel.send(2)
//            }

            val scope = CoroutineScope(Dispatchers.IO)
            val channel = scope.produce {
                repeat(100) {
                    send(it)
                }
            }

            val job1 = launch {
                println("Receive ${channel.receive()}")
                println("Receive ${channel.receive()}")
            }

            joinAll(job1)
        }
    }
}