package info.ditrapani.board

import info.ditrapani.model.Color
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import java.lang.IllegalArgumentException

data class PatternLine(var color: Color, var count: Int) {
    fun add(newColor: Color, newCount: Int, max: Int): Int {
        if (color != newColor) {
            throw IllegalArgumentException("cheating!")
        }
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

fun PatternLine?.add(newColor: Color, newCount: Int, max: Int): Int {
    val patternLine = if (this == null) {
        PatternLine(newColor, 0)
    } else {
        this
    }
    return patternLine.add(newColor, newCount, max)
}