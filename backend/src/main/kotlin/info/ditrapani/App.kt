package info.ditrapani

import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle

private const val PORT = 44777

class Server : CoroutineVerticle() {
    fun hi(): String = "hello"

    override suspend fun start() {
        val router = Router.router(vertx)
        router.get("/hello").handler { routingContext ->
            routingContext.response().end("hello!!!")
        }
        val server = vertx.createHttpServer().requestHandler(router)
        server.listenAwait(PORT)
        println("Test server listening on port $PORT")
    }
}

fun main() {
    println(Server().hi())
    Vertx.vertx().deployVerticle(Server())
}
