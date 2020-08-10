package info.ditrapani.factory

import info.ditrapani.game.Supply
import info.ditrapani.game.Trash
import info.ditrapani.game.getTile
import info.ditrapani.model.Color
import info.ditrapani.model.Maybe
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.array
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

data class Display(
    val slot1: Color?,
    val slot2: Color?,
    val slot3: Color?,
    val slot4: Color?
) {
    fun toJsonArray(): JsonArray =
        json {
            array(
                slot1?.toString(),
                slot2?.toString(),
                slot3?.toString(),
                slot4?.toString()
            )
        }
}

fun newDisplay(supply: Supply, trash: Trash) =
    Display(
        supply.getTile(trash),
        supply.getTile(trash),
        supply.getTile(trash),
        supply.getTile(trash)
    )

data class Leftovers(
    val whites: Int,
    val reds: Int,
    val blues: Int,
    val greens: Int,
    val blacks: Int,
    val nextFirstPlayer: Maybe
) {
    fun toJson(): JsonObject =
        json {
            obj(
                "whites" to whites,
                "reds" to reds,
                "blues" to blues,
                "greens" to greens,
                "blacks" to blacks,
                "nextFirstPlayer" to nextFirstPlayer.toString()
            )
        }
}

val newLeftovers = Leftovers(0, 0, 0, 0, 0, Maybe.PRESENT)

data class Factory(val displays: List<Display>, val leftovers: Leftovers) {
    fun toJson(): JsonObject =
        json {
            obj(
                "displays" to JsonArray(displays.map { it.toJsonArray() }),
                "leftovers" to leftovers.toJson()
            )
        }
}

fun newFactoryFromSupply(supply: Supply, trash: Trash): Factory {
    val displays = 0.until(7).map { newDisplay(supply, trash) }
    return Factory(displays, newLeftovers)
}
