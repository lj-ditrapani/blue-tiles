package info.ditrapani.board

import info.ditrapani.model.Color
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

data class PatternLine(var color: Color, var count: Int) {
    fun add(newCount: Int, max: Int): Int {
        val total = count + newCount
        return if (total > max) {
            count = max
            total - max
        } else {
            count = total
            0
        }
    }

    fun toJson(): JsonObject =
        json { obj("color" to color.toString(), "count" to count) }
}

fun PatternLine?.add(
    newColor: Color,
    newCount: Int,
    max: Int,
    saveLine: (PatternLine) -> Unit
): Int {
    val patternLine = if (this == null) {
        val temp = PatternLine(newColor, 0)
        saveLine(temp)
        temp
    } else {
        this
    }
    return patternLine.add(newCount, max)
}

fun PatternLine?.isValidColor(newColor: Color, max: Int): Boolean =
    if (this == null) {
        true
    } else {
        count < max && color == newColor
    }
