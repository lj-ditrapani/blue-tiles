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
    val c1: Maybe,
    val c2: Maybe,
    val c3: Maybe,
    val c4: Maybe,
    val c5: Maybe
)

val newGridRow = GridRow(
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

val newGrid =
    Grid(
        newGridRow,
        newGridRow,
        newGridRow,
        newGridRow,
        newGridRow
    )

data class Board(
    val row1: StorageRow?,
    val row2: StorageRow?,
    val row3: StorageRow?,
    val row4: StorageRow?,
    val row5: StorageRow?,
    val grid: Grid,
    val nextFirstPlayer: Maybe,
    val floor: List<Color>
) {
    fun toJson(): JsonObject =
        json {
            obj(
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
    null,
    null,
    null,
    null,
    null,
    newGrid,
    Maybe.MISSING,
    listOf()
)
