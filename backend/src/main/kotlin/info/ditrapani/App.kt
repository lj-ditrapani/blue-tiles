package info.ditrapani

import info.ditrapani.game.Game
import info.ditrapani.game.newGame
import info.ditrapani.model.Player
import info.ditrapani.model.parsePlay
import io.vertx.core.Vertx
import io.vertx.core.http.CookieSameSite
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.JsonObject
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
                routingContext.response().sendJson(
                    json {
                        obj(
                            "status" to "unregistered",
                            "result" to "Game is full; you are a spectator"
                        )
                    }
                )
            } else {
                session.put("player", player)
                routingContext.response().sendJson(
                    json {
                        obj(
                            "player" to player.name,
                            "status" to "registered"
                        )
                    }
                )
            }
        }
        router.get("/ready").handler { routingContext ->
            val body = json {
                obj(
                    "ready" to playerCounter.isReady(),
                    "count" to playerCounter.count()
                )
            }
            routingContext.response().sendJson(body)
        }
        router.get("/status").handler { routingContext ->
            val session = routingContext.session()
            val player = session.get<Player?>("player")
            val response = routingContext.response()
            response.putHeader("Content-Type", "application/json")
            response.end(game.toJson(player).toString())
        }
        router.post("/play/:location/:color/:moveTo").handler { routingContext ->
            val session = routingContext.session()
            val player: Player? = session.get<Player?>("player")
            logger.info("Player: $player")
            if (player != game.currentPlayer) {
                routingContext.response()
                    .setStatusCode(400)
                    .sendJson(json { obj("error" to "Not your turn!") })
            } else {
                val request = routingContext.request()
                val location: String? = request.getParam("location")
                val color: String? = request.getParam("color")
                val moveTo: String? = request.getParam("moveTo")
                logger.info("location: $location color: $color moveTo: $moveTo")
                val play = parsePlay(player, location, color, moveTo)
                val result = if (play == null) {
                    Failure
                } else {
                    game.update(play)
                }
                when (result) {
                    is Success ->
                        routingContext
                            .response()
                            .sendJson(json { obj("result" to result.toString()) })
                    Failure ->
                        routingContext
                            .response()
                            .setStatusCode(400)
                            .sendJson(json { obj("error" to "Illegal move") })
                }
            }
        }
        val server = vertx.createHttpServer().requestHandler(router)
        server.listenAwait(PORT)
        println("Test server listening on port $PORT")
    }
}

fun HttpServerResponse.sendJson(json: JsonObject): HttpServerResponse {
    putHeader("Content-Type", "application/json")
    end(json.toString())
    return this
}

fun main() {
    val logger = LogManager.getLogger("Server")
    Vertx.vertx().deployVerticle(Server(PlayerCounter(), logger))
}
