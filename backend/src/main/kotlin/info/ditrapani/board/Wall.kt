package info.ditrapani.board

import info.ditrapani.model.Color
import info.ditrapani.model.Maybe
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

data class WallLine(
    var c1: Maybe,
    var c2: Maybe,
    var c3: Maybe,
    var c4: Maybe,
    var c5: Maybe
) {
    fun isComplete(): Boolean =
        listOf(c1, c2, c3, c4, c5).all { it == Maybe.PRESENT }

    fun toJson(): JsonObject =
        json {
            obj(
                "c1" to c1.toString(),
                "c2" to c2.toString(),
                "c3" to c3.toString(),
                "c4" to c4.toString(),
                "c5" to c5.toString()
            )
        }

    fun setColumn(index: Int) {
        when (index) {
            0 -> c1 = Maybe.PRESENT
            1 -> c2 = Maybe.PRESENT
            2 -> c3 = Maybe.PRESENT
            3 -> c4 = Maybe.PRESENT
            else -> c5 = Maybe.PRESENT
        }
    }

    fun isColumnSet(index: Int): Boolean =
        when (index) {
            0 -> c1 == Maybe.PRESENT
            1 -> c2 == Maybe.PRESENT
            2 -> c3 == Maybe.PRESENT
            3 -> c4 == Maybe.PRESENT
            else -> c5 == Maybe.PRESENT
        }
}

fun newGridRow(): WallLine = WallLine(
    Maybe.MISSING,
    Maybe.MISSING,
    Maybe.MISSING,
    Maybe.MISSING,
    Maybe.MISSING
)

data class Wall(
    val line1: WallLine,
    val line2: WallLine,
    val line3: WallLine,
    val line4: WallLine,
    val line5: WallLine
) {
    fun isGameOver(): Boolean =
        listOf(line1, line2, line3, line4, line5).any { it.isComplete() }

    fun getLine(index: Int): WallLine =
        when (index) {
            0 -> line1
            1 -> line2
            2 -> line3
            3 -> line4
            else -> line5
        }

    fun isOccupied(row: Int, color: Color): Boolean {
        val line = listOf(line1, line2, line3, line4, line5)[row - 1]
        val columnIndex = getColumnFromColorRow(color, row)
        return line.isColumnSet(columnIndex)
    }

    fun toJson(): JsonObject =
        json {
            obj(
                "line1" to line1.toJson(),
                "line2" to line2.toJson(),
                "line3" to line3.toJson(),
                "line4" to line4.toJson(),
                "line5" to line5.toJson()
            )
        }

    fun computeRowBonus(): Int {
        return 2 * listOf(line1, line2, line3, line4, line5).count { it.isComplete() }
    }

    fun computeColumnBonus(): Int {
        return 7 * 0.until(5).count { isRowComplete(it) }
    }

    fun computeColorBonus(): Int {
        return 10 * Color.values().count { color ->
            listOf(
                1 to line1,
                2 to line2,
                3 to line3,
                4 to line4,
                5 to line5
            ).all { (row, line) ->
                line.isColumnSet(getColumnFromColorRow(color, row))
            }
        }
    }

    private fun isRowComplete(index: Int): Boolean =
        listOf(line1, line2, line3, line4, line5).all { it.isColumnSet(index) }
}

fun newWall(): Wall =
    Wall(
        newGridRow(),
        newGridRow(),
        newGridRow(),
        newGridRow(),
        newGridRow()
    )

fun getColumnFromColorRow(color: Color, row: Int): Int =
    (color.index + (row - 1)) % 5
