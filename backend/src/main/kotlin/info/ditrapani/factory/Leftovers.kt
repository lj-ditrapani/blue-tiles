package info.ditrapani.factory

import info.ditrapani.model.Color
import info.ditrapani.model.Maybe
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

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

    fun cleanup(display: Display) {
        addColor(display.slot1)
        display.slot1 = null
        addColor(display.slot2)
        display.slot2 = null
        addColor(display.slot3)
        display.slot3 = null
        addColor(display.slot4)
        display.slot4 = null
    }

    fun addColor(color: Color?) {
        when (color) {
            null -> Unit
            Color.WHITE -> whites += 1
            Color.RED -> reds += 1
            Color.BLUE -> blues += 1
            Color.GREEN -> greens += 1
            Color.BLACK -> blacks += 1
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
