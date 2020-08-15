package info.ditrapani.board

import info.ditrapani.model.Color
import info.ditrapani.model.Maybe
import info.ditrapani.model.PlayRecord
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.array
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
    fun update(playRecord: PlayRecord) {
        val tileCount = playRecord.tileCount
        val play = playRecord.play
        val color = play.color
        val row = play.row
        when (row) {
            1 -> doUpdate(line1, color, tileCount, 1)
            2 -> doUpdate(line2, color, tileCount, 2)
            3 -> doUpdate(line3, color, tileCount, 3)
            4 -> doUpdate(line4, color, tileCount, 4)
            5 -> doUpdate(line5, color, tileCount, 5)
        }
    }

    private fun doUpdate(row: PatternLine?, color: Color, tileCount: Int, max: Int) {
        val floorTileCount = row.add(color, tileCount, max)
        0.until(floorTileCount).forEach {
            floor.add(color)
        }
    }

    fun isGameOver(): Boolean = wall.isGameOver()

    fun toJson(): JsonObject =
        json {
            obj(
                "score" to score,
                "rows" to json {
                    array(
                        line1?.toJson(),
                        line2?.toJson(),
                        line3?.toJson(),
                        line4?.toJson(),
                        line5?.toJson()
                    )
                },
                "grid" to wall.toString(),
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
    newWall(),
    Maybe.MISSING,
    mutableListOf()
)
