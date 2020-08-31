package info.ditrapani.factory

import info.ditrapani.game.Supply
import info.ditrapani.game.Trash
import info.ditrapani.model.DisplayLocation
import info.ditrapani.model.LeftoversLocation
import info.ditrapani.model.Maybe
import info.ditrapani.model.Play
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

data class Factory(val displays: List<Display>, val leftovers: Leftovers) {
    fun isEmpty(): Boolean = leftovers.isEmpty() && displays.all { it.isEmpty() }

    fun reset(supply: Supply, trash: Trash) {
        displays.forEach { it.reload(supply, trash) }
        leftovers.nextFirstPlayer = Maybe.PRESENT
    }

    fun isPlayValid(play: Play): Boolean =
        when (play.location) {
            is DisplayLocation ->
                displays[play.location.number - 1].isColorValid(play.color)
            LeftoversLocation ->
                leftovers.isColorValid(play.color)
        }

    fun update(play: Play): FactoryUpdate {
        val location = play.location
        return when (location) {
            is DisplayLocation -> {
                val display = displays[location.number - 1]
                val count = display.take(play.color)
                leftovers.cleanup(display)
                FactoryUpdate(count, Maybe.MISSING)
            }
            LeftoversLocation -> {
                val count = leftovers.take(play.color)
                val firstPlayer = leftovers.nextFirstPlayer
                if (firstPlayer == Maybe.PRESENT) {
                    leftovers.nextFirstPlayer = Maybe.MISSING
                }
                FactoryUpdate(count, firstPlayer)
            }
        }
    }

    fun toJson(): JsonObject =
        json {
            obj(
                "displays" to JsonArray(displays.map { it.toJson() }),
                "leftovers" to leftovers.toJson()
            )
        }
}

fun newFactoryFromSupply(supply: Supply, trash: Trash): Factory {
    val displays = 0.until(7).map { newDisplay(supply, trash) }
    return Factory(displays, newLeftovers)
}

data class FactoryUpdate(val tileCount: Int, val firstPlayer: Maybe)
