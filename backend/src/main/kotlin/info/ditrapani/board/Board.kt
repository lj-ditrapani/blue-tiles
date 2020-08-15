package info.ditrapani.board

import info.ditrapani.model.Color
import info.ditrapani.model.Maybe
import info.ditrapani.model.PlayRecord
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.array
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import java.lang.IllegalArgumentException

data class StorageRow(var color: Color, var count: Int) {
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

fun StorageRow?.add(newColor: Color, newCount: Int, max: Int): Int {
    val storageRow = if (this == null) {
        StorageRow(newColor, 0)
    } else {
        this
    }
    return storageRow.add(newColor, newCount, max)
}

data class GridRow(
    var c1: Maybe,
    var c2: Maybe,
    var c3: Maybe,
    var c4: Maybe,
    var c5: Maybe
)

fun newGridRow(): GridRow = GridRow(
    Maybe.MISSING,
    Maybe.MISSING,
    Maybe.MISSING,
    Maybe.MISSING,
    Maybe.MISSING
)

data class Grid(
    val row1: GridRow,
    val row2: GridRow,
    val row3: GridRow,
    val row4: GridRow,
    val row5: GridRow
)

fun newGrid(): Grid =
    Grid(
        newGridRow(),
        newGridRow(),
        newGridRow(),
        newGridRow(),
        newGridRow()
    )

data class Board(
    var score: Int,
    var row1: StorageRow?,
    var row2: StorageRow?,
    var row3: StorageRow?,
    var row4: StorageRow?,
    var row5: StorageRow?,
    val grid: Grid,
    var nextFirstPlayer: Maybe,
    val floor: MutableList<Color>
) {
    fun update(playRecord: PlayRecord) {
        val tileCount = playRecord.tileCount
        val play = playRecord.play
        val color = play.color
        val row = play.row
        when (row) {
            1 -> doUpdate(row1, color, tileCount, 1)
            2 -> doUpdate(row2, color, tileCount, 2)
            3 -> doUpdate(row3, color, tileCount, 3)
            4 -> doUpdate(row4, color, tileCount, 4)
            5 -> doUpdate(row5, color, tileCount, 5)
        }
    }

    private fun doUpdate(row: StorageRow?, color: Color, tileCount: Int, max: Int) {
        val floorTileCount = row.add(color, tileCount, max)
        0.until(floorTileCount).forEach {
            floor.add(color)
        }
    }

    fun toJson(): JsonObject =
        json {
            obj(
                "score" to score,
                "rows" to json {
                    array(
                        row1?.toJson(),
                        row2?.toJson(),
                        row3?.toJson(),
                        row4?.toJson(),
                        row5?.toJson()
                    )
                },
                "grid" to grid.toString(),
                "nextFirstPlayer" to nextFirstPlayer.toString(),
                "floor" to JsonArray(floor.map { it.toString() })
            )
        }
}

val newBoard = Board(
    0,
    null,
    null,
    null,
    null,
    null,
    newGrid(),
    Maybe.MISSING,
    mutableListOf()
)
