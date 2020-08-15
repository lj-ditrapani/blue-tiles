package info.ditrapani.model

import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

data class Play(
    val player: Player,
    val location: Location,
    val color: Color,
    val moveTo: MoveTo
)

fun parsePlay(player: Player, location: String, color: String, moveTo: String): Play =
    Play(player, parseLocation(location), Color.valueOf(color), parseMoveTo(moveTo))

sealed class Location
object LeftoversLocation : Location()
data class DisplayLocation(val number: Int) : Location()

fun parseLocation(str: String): Location =
    if (str == "leftovers") {
        LeftoversLocation
    } else {
        DisplayLocation(str.toInt())
    }

data class PlayRecord(val tileCount: Int, val play: Play) {
    fun toJson(): JsonObject =
        json {
            obj(
                "player" to play.player.name,
                "location" to play.location.toString(),
                "color" to play.color.name,
                "tileCount" to tileCount,
                "moveTo" to play.moveTo.toString()
            )
        }
}

fun PlayRecord?.toJson(): JsonObject? =
    this?.toJson()

sealed class MoveTo
object MoveToFloor : MoveTo() {
    override fun toString() = "floor"
}
data class MoveToRow(val row: Int) : MoveTo() {
    override fun toString() = "row$row"
}

fun parseMoveTo(str: String): MoveTo =
    if (str == "floor") {
        MoveToFloor
    } else {
        MoveToRow(str[3].toInt())
    }
