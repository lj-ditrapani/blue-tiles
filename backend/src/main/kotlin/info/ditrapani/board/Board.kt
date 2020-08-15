package info.ditrapani.board

import info.ditrapani.model.Color
import info.ditrapani.model.Maybe
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.array
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

data class StorageRow(val color: Color, val count: Int) {
    fun toJson(): JsonObject =
        json { obj("color" to color.toString(), "count" to count) }
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
