package info.ditrapani

import info.ditrapani.game.Game
import info.ditrapani.game.newGame
import info.ditrapani.model.Player
import io.vertx.core.Vertx
import io.vertx.core.http.CookieSameSite
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.SessionHandler
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.sstore.LocalSessionStore
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.kotlin.coroutines.CoroutineVerticle
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

private const val PORT = 44777

class Server(
    private val playerCounter: PlayerCounter,
    private val logger: Logger
) : CoroutineVerticle() {
    fun hi(): String = "hello"
    val game: Game = newGame()

    override suspend fun start() {
        val sessionStore = LocalSessionStore.create(vertx)
        val sessionHandler = SessionHandler.create(sessionStore)
        sessionHandler.setCookieSameSite(CookieSameSite.STRICT)
        val router = Router.router(vertx)
        router.route("/*").handler(StaticHandler.create())
        router.route().handler(sessionHandler)
        router.get("/hello").handler { routingContext ->
            routingContext.response().end("hello!!!")
        }
        router.post("/register").handler { routingContext ->
            val session = routingContext.session()
            val player = playerCounter.getAndInc()
            if (player == null) {
                routingContext.response().end("Game is full; you are a spectator")
            } else {
                session.put("player", player)
                routingContext.response().end("registered as player # $player")
            }
        }
        router.get("/ready").handler { routingContext ->
            val body = json {
                obj(
                    "ready" to playerCounter.isReady(),
                    "count" to playerCounter.count()
                )
            }
            routingContext.response().end(body.toString())
        }
        router.get("/status").handler { routingContext ->
            val session = routingContext.session()
            val player = session.get<Player?>("player")
            val response = routingContext.response()
            response.putHeader("Content-Type", "application/json")
            response.end(game.toJson(player).toString())
        }
        router.post("/play/:location/:color/:row").handler { routingContext ->
            val session = routingContext.session()
            val playerNumber = session.get<Int?>("playerNumber")
            logger.info("Player number: $playerNumber")
            val request = routingContext.request()
            val location = request.getParam("location")
            val color = request.getParam("color")
            val row = request.getParam("row")
            logger.info("location: $location color: $color row: $row")
            val response = routingContext.response()
            response.putHeader("Content-Type", "application/json")
            response.end("play...")
        }
        val server = vertx.createHttpServer().requestHandler(router)
        server.listenAwait(PORT)
        println("Test server listening on port $PORT")
    }
}

class PlayerCounter {
    private var i = 0

    fun isReady(): Boolean =
        if (i == 3) {
            true
        } else {
            false
        }

    fun count(): Int = i

    fun getAndInc(): Player? =
        if (i == 3) {
            null
        } else {
            i = i + 1
            Player.valueOf("P$i")
        }
}

fun main() {
    val logger = LogManager.getLogger("Server")
    Vertx.vertx().deployVerticle(Server(PlayerCounter(), logger))
}
