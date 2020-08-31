package info.ditrapani.board

import info.ditrapani.Result
import info.ditrapani.Success
import info.ditrapani.model.Color
import info.ditrapani.model.Maybe
import info.ditrapani.model.MoveToFloor
import info.ditrapani.model.MoveToRow
import info.ditrapani.model.PlayRecord
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

data class Board(
    var score: Int,
    var line1: PatternLine?,
    var line2: PatternLine?,
    var line3: PatternLine?,
    var line4: PatternLine?,
    var line5: PatternLine?,
    val wall: Wall,
    var nextFirstPlayer: Maybe,
    val floor: MutableList<Color>
) {
    fun update(playRecord: PlayRecord): Result<Unit> {
        val tileCount = playRecord.tileCount
        val play = playRecord.play
        val color = play.color
        val moveTo = play.moveTo
        when (moveTo) {
            is MoveToRow ->
                when (moveTo.row) {
                    1 -> updateLine(line1, color, tileCount, 1, { line -> line1 = line })
                    2 -> updateLine(line2, color, tileCount, 2, { line -> line2 = line })
                    3 -> updateLine(line3, color, tileCount, 3, { line -> line3 = line })
                    4 -> updateLine(line4, color, tileCount, 4, { line -> line4 = line })
                    5 -> updateLine(line5, color, tileCount, 5, { line -> line5 = line })
                }
            MoveToFloor ->
                updateFloor(color, tileCount)
        }
        nextFirstPlayer = playRecord.firstPlayer
        return Success(Unit)
    }

    private fun updateLine(
        line: PatternLine?,
        color: Color,
        tileCount: Int,
        max: Int,
        saveLine: (PatternLine) -> Unit
    ): Result<Unit> =
        line.add(color, tileCount, max, saveLine).map { floorTileCount ->
            0.until(floorTileCount).forEach {
                floor.add(color)
            }
            Unit
        }

    private fun updateFloor(color: Color, tileCount: Int) {
        0.until(tileCount).forEach {
            floor.add(color)
        }
    }

    fun isGameOver(): Boolean = wall.isGameOver()

    fun toJson(): JsonObject =
        json {
            obj(
                "score" to score,
                "line1" to line1?.toJson(),
                "line2" to line2?.toJson(),
                "line3" to line3?.toJson(),
                "line4" to line4?.toJson(),
                "line5" to line5?.toJson(),
                "wall" to wall.toJson(),
                "nextFirstPlayer" to nextFirstPlayer.toString(),
                "floor" to JsonArray(floor.map { it.toString() })
            )
        }
}

fun newBoard(): Board = Board(
    0,
    null,
    null,
    null,
    null,
    null,
    newWall(),
    Maybe.MISSING,
    mutableListOf()
)
