import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.wss
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import kotlinx.coroutines.channels.filterNotNull
import kotlinx.coroutines.channels.map

suspend fun main() {
    try {
        println("start")

        val client = HttpClient(CIO).config { install(WebSockets) }

        client.wss(host = "echo.websocket.org") {
            send(Frame.Text("Hello World"))

            for (message in incoming.map { it as? Frame.Text }.filterNotNull()) {
                println(message.readText())
            }
        }

        println("start finished")

    } catch (e: Exception) {
        e.printStackTrace()
        println("stopped abnormal")
    }
}
