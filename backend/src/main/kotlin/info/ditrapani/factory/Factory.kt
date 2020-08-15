package info.ditrapani.factory

import info.ditrapani.game.Supply
import info.ditrapani.game.Trash
import info.ditrapani.game.getTile
import info.ditrapani.model.Color
import info.ditrapani.model.FactoryDisplay
import info.ditrapani.model.Maybe
import info.ditrapani.model.Play
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.array
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

data class Display(
    var slot1: Color?,
    var slot2: Color?,
    var slot3: Color?,
    var slot4: Color?
) {
    fun isEmpty(): Boolean =
        slot1 == null &&
            slot2 == null &&
            slot3 == null &&
            slot4 == null

    fun reload(supply: Supply, trash: Trash) {
        slot1 = supply.getTile(trash)
        slot2 = supply.getTile(trash)
        slot3 = supply.getTile(trash)
        slot4 = supply.getTile(trash)
    }

    fun take(color: Color): Int {
        var count = 0
        if (slot1 != null && slot1 == color) {
            count += 1
            slot1 = null
        }
        if (slot2 != null && slot2 == color) {
            count += 1
            slot2 = null
        }
        if (slot3 != null && slot3 == color) {
            count += 1
            slot3 = null
        }
        if (slot4 != null && slot4 == color) {
            count += 1
            slot4 = null
        }
        return count
    }

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
    var whites: Int,
    var reds: Int,
    var blues: Int,
    var greens: Int,
    var blacks: Int,
    var nextFirstPlayer: Maybe
) {
    fun isEmpty(): Boolean =
        nextFirstPlayer == Maybe.MISSING &&
            whites == 0 &&
            reds == 0 &&
            blues == 0 &&
            greens == 0 &&
            blacks == 0

    fun take(color: Color): Int =
        when (color) {
            Color.WHITE -> {
                val temp = whites
                whites = 0
                temp
            }
            Color.RED -> {
                val temp = reds
                reds = 0
                temp
            }
            Color.BLUE -> {
                val temp = blues
                blues = 0
                temp
            }
            Color.GREEN -> {
                val temp = greens
                greens = 0
                temp
            }
            Color.BLACK -> {
                val temp = blacks
                blacks = 0
                temp
            }
        }

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
    fun isEmpty(): Boolean = leftovers.isEmpty() && displays.all { it.isEmpty() }

    fun reset(supply: Supply, trash: Trash) {
        displays.forEach { it.reload(supply, trash) }
        leftovers.nextFirstPlayer = Maybe.PRESENT
    }

    fun applyPlay(play: Play): Int {
        val location = play.location
        return when (location) {
            is FactoryDisplay ->
                displays[location.number - 1].take(play.color)
            info.ditrapani.model.Leftovers ->
                leftovers.take(play.color)
        }
    }

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
