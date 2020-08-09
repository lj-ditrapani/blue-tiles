package info.ditrapani

import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

private const val PORT = 44777

class Server(private val logger: Logger) : CoroutineVerticle() {
    fun hi(): String = "hello"

    override suspend fun start() {
        val router = Router.router(vertx)
        router.route("/*").handler(StaticHandler.create())
        router.get("/hello").handler { routingContext ->
            routingContext.response().end("hello!!!")
        }
        router.post("/register").handler { routingContext ->
            // assigns a player # to the session ID
            routingContext.response().end("hello!!!")
        }
        router.get("/status").handler { routingContext ->
            val response = routingContext.response()
            response.putHeader("Content-Type", "application/json")
            response.end("status...")
        }
        router.post("/move/:location/:color/:row").handler { routingContext ->
            val request = routingContext.request()
            val location = request.getParam("location")
            val color = request.getParam("color")
            val row = request.getParam("row")
            logger.info("location: $location color: $color row: $row")
            val response = routingContext.response()
            response.putHeader("Content-Type", "application/json")
            response.end("move...")
        }
        val server = vertx.createHttpServer().requestHandler(router)
        server.listenAwait(PORT)
        println("Test server listening on port $PORT")
    }
}

fun main() {
    val logger = LogManager.getLogger("Server")
    Vertx.vertx().deployVerticle(Server(logger))
}
