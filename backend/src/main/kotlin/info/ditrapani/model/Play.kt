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

fun parsePlay(player: Player, location: String?, color: String?, moveTo: String?): Play? {
    val newLocation = parseLocation(location)
    val newColor = parseColor(color)
    val newMoveTo = parseMoveTo(moveTo)
    return if (newLocation != null && newColor != null && newMoveTo != null) {
        Play(player, newLocation, newColor, newMoveTo)
    } else {
        null
    }
}

sealed class Location
object LeftoversLocation : Location() {
    override fun toString(): String = "leftovers"
}
data class DisplayLocation(val number: Int) : Location() {
    override fun toString(): String = "Display # $number"
}

fun parseLocation(str: String?): Location? =
    str?.let { notNull ->
        if (notNull == "leftovers") {
            LeftoversLocation
        } else {
            val maybeNumber = try {
                notNull.toInt()
            } catch (error: NumberFormatException) {
                null
            }
            maybeNumber?.let { number ->
                if (number >= 1 && number <= 7) {
                    DisplayLocation(number)
                } else {
                    null
                }
            }
        }
    }

data class PlayRecord(val tileCount: Int, val firstPlayer: Maybe, val play: Play) {
    fun toJson(): JsonObject =
        json {
            obj(
                "player" to play.player.name,
                "location" to play.location.toString(),
                "color" to play.color.name,
                "tileCount" to tileCount,
                "nextFirstPlayer" to firstPlayer,
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
    override fun toString() = "line # $row"
}

fun parseMoveTo(str: String?): MoveTo? =
    str?.let { notNull ->
        if (notNull == "floor") {
            MoveToFloor
        } else {
            val maybeNumber = try {
                notNull.toInt()
            } catch (error: NumberFormatException) {
                null
            }
            maybeNumber?.let { number ->
                if (number >= 1 && number <= 5) {
                    MoveToRow(number)
                } else {
                    null
                }
            }
        }
    }
