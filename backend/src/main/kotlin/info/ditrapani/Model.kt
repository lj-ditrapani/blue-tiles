package info.ditrapani

import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.array
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

enum class Player {
    P1,
    P2,
    P3
}

enum class Color {
    WHITE,
    RED,
    BLUE,
    GREEN,
    BLACK
}

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

fun newDisplay(): Display = Display(null, null, null, null)

enum class Maybe {
    PRESENT,
    MISSING
}

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

fun newLeftovers(): Leftovers =
    Leftovers(0, 0, 0, 0, 0, Maybe.PRESENT)

data class Factory(val displays: List<Display>, val leftovers: Leftovers) {
    fun toJson(): JsonObject =
        json {
            obj(
                "displays" to JsonArray(displays.map { it.toJsonArray() }),
                "leftovers" to leftovers.toJson()
            )
        }
}

fun newFactory(): Factory = Factory(
    0.until(7).map { newDisplay() },
    newLeftovers()
)

data class Trash(
    val whites: Int,
    val reds: Int,
    val blues: Int,
    val greens: Int,
    val blacks: Int
) {
    fun size(): Int = whites + reds + blues + greens + blacks
}

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

data class Grid(
    val row1: GridRow,
    val row2: GridRow,
    val row3: GridRow,
    val row4: GridRow,
    val row5: GridRow
)

data class Board(
    val row1: StorageRow,
    val row2: StorageRow,
    val row3: StorageRow,
    val row4: StorageRow,
    val row5: StorageRow,
    val grid: Grid,
    val nextFirstPlayer: Maybe,
    val floor: List<Color>
) {
    fun toJson(): JsonObject =
        json {
            obj(
                "rows" to json {
                    array(
                        row1.toJson(),
                        row2.toJson(),
                        row3.toJson(),
                        row4.toJson(),
                        row5.toJson()
                    )
                },
                "grid" to grid.toString(),
                "nextFirstPlayer" to nextFirstPlayer.toString(),
                "floor" to JsonArray(floor.map { it.toString() })
            )
        }
}

fun newBoard(): Board = TODO()

data class Game(
    val currentFirstPlayer: Player,
    val currentPlayer: Player,
    val factory: Factory,
    val supply: List<Color>,
    val trash: Trash,
    val board1: Board,
    val board2: Board,
    val board3: Board
) {
    fun toJson(): JsonObject =
        json {
            obj(
                "currentPlayer" to currentPlayer,
                "factory" to factory.toJson(),
                "supplyCount" to supply.size,
                "trashCount" to trash.size(),
                "board1" to board1.toJson(),
                "board2" to board1.toJson(),
                "board3" to board1.toJson()
            )
        }
}

fun newGame(): Game =
    Game(
        Player.P1,
        Player.P1,
        newFactory(),
        listOf(),
        Trash(0, 0, 0, 0, 0),
        newBoard(),
        newBoard(),
        newBoard()
    )
