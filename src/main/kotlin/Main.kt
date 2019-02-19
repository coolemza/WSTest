import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.webSocketRawSession
import io.ktor.client.features.websocket.wss
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@KtorExperimentalAPI
suspend fun main() = try {
    println("start")

    repeat(10) { cnt ->
        println("try #$cnt")
        val client = HttpClient( CIO).config { install(WebSockets) }
        client.wss(host = "echo.websocket.org") {
            repeat(10) { send(Frame.Text("Hello World $it")) }

            GlobalScope.launch(Dispatchers.IO) {
                repeat(10) {
                    println((incoming.receive() as Frame.Text).readText())
                }
            }.join()

            close(CloseReason(1000, "stop"))
        }
    }

    println("start finished")

} catch (e: Exception) {
    e.printStackTrace()
    println("stopped abnormal")
}
