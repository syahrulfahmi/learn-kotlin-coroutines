import org.junit.jupiter.api.Test
import java.util.Date
import kotlin.concurrent.thread

class ThreadTest {

    @Test
    fun testThreadName() {
        val thread = Thread.currentThread().name
        print(thread)
    }

    /**
     * Membuat Thread baru
     */
    @Test
    fun testNewThread() {
        /** default thread dengan java */
//        val runnable = Runnable {
//            println(Date())
//            Thread.sleep(2_000)
//            println("Finish: ${Date()}")
//        }
        /** membuat thread dengan fungsi kotlin */
        thread {
            println(Date())
            Thread.sleep(2_000)
            println("Finish: ${Date()}")
        }
//        val thread = Thread(runnable)
//        thread.start()
        println("MENUNGGU SELESAI")
        Thread.sleep(3_000)
        println("SELESAI")
    }

    /**
     * Membuat multiple thread
     */
    @Test
    fun testMultipleThread() {
        thread {
            println(Date())
            Thread.sleep(2_000)
            println("Finish: ${Date()} di Thread: ${Thread.currentThread().name}")
        }
        thread {
            println(Date())
            Thread.sleep(2_000)
            println("Finish: ${Date()} di Thread: ${Thread.currentThread().name}")
        }
        println("MENUNGGU SELESAI")
        Thread.sleep(3_000)
        println("SELESAI")
    }
}