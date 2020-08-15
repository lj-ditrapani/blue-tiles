package info.ditrapani.model

import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

enum class Color {
    WHITE,
    RED,
    BLUE,
    GREEN,
    BLACK
}

enum class Maybe {
    PRESENT,
    MISSING
}

enum class Player {
    P1,
    P2,
    P3
}

data class Play(
    val player: Player,
    val location: Location,
    val color: Color,
    val row: Int
)

sealed class Location
object Leftovers : Location()
data class FactoryDisplay(val number: Int) : Location()

fun parseLocation(str: String): Location =
    if (str == "leftovers") {
        Leftovers
    } else {
        FactoryDisplay(str.toInt())
    }

data class PlayRecord(val tileCount: Int, val play: Play) {
    fun toJson(): JsonObject =
        json {
            obj(
                "player" to play.player.name,
                "location" to play.location.toString(),
                "color" to play.color.name,
                "tileCount" to tileCount,
                "row" to play.row
            )
        }
}

fun PlayRecord?.toJson(): JsonObject? =
    if (this == null) {
        null
    } else {
        toJson()
    }
