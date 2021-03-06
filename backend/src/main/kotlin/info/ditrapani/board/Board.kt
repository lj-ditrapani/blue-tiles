package info.ditrapani.board

import info.ditrapani.game.Trash
import info.ditrapani.model.Color
import info.ditrapani.model.Maybe
import info.ditrapani.model.MoveToFloor
import info.ditrapani.model.MoveToRow
import info.ditrapani.model.Play
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
    fun isPlayValid(play: Play): Boolean =
        when (play.moveTo) {
            is MoveToRow -> {
                if (wall.isOccupied(play.moveTo.row, play.color)) {
                    false
                } else {
                    when (play.moveTo.row) {
                        1 -> line1.isValidColor(play.color, 1)
                        2 -> line2.isValidColor(play.color, 2)
                        3 -> line3.isValidColor(play.color, 3)
                        4 -> line4.isValidColor(play.color, 4)
                        else -> line5.isValidColor(play.color, 5)
                    }
                }
            }
            MoveToFloor ->
                true
        }

    fun update(playRecord: PlayRecord, trash: Trash) {
        if (playRecord.firstPlayer == Maybe.PRESENT) {
            nextFirstPlayer = Maybe.PRESENT
        }
        val tileCount = playRecord.tileCount
        val play = playRecord.play
        val color = play.color
        val moveTo = play.moveTo
        when (moveTo) {
            is MoveToRow ->
                when (moveTo.row) {
                    1 -> updateLine(line1, color, tileCount, 1, trash) { line ->
                        line1 = line
                    }
                    2 -> updateLine(line2, color, tileCount, 2, trash) { line ->
                        line2 = line
                    }
                    3 -> updateLine(line3, color, tileCount, 3, trash) { line ->
                        line3 = line
                    }
                    4 -> updateLine(line4, color, tileCount, 4, trash) { line ->
                        line4 = line
                    }
                    5 -> updateLine(line5, color, tileCount, 5, trash) { line ->
                        line5 = line
                    }
                }
            MoveToFloor ->
                updateFloor(color, tileCount, trash)
        }
    }

    fun tileWall(trash: Trash) {
        tileLine(line1, wall.line1, 1, trash) { line1 = null }
        tileLine(line2, wall.line2, 2, trash) { line2 = null }
        tileLine(line3, wall.line3, 3, trash) { line3 = null }
        tileLine(line4, wall.line4, 4, trash) { line4 = null }
        tileLine(line5, wall.line5, 5, trash) { line5 = null }
    }

    fun cleanFloor(trash: Trash) {
        score -= getPenalty(floor, nextFirstPlayer)
        if (score < 0) {
            score = 0
        }
        floor.forEach { color ->
            trash.add(color, 1)
        }
        floor.clear()
    }

    fun addBonusScore() {
        score += wall.computeRowBonus()
        score += wall.computeColumnBonus()
        score += wall.computeColorBonus()
    }

    private fun getPenalty(floor: MutableList<Color>, nextFirstPlayer: Maybe): Int {
        val extra = if (nextFirstPlayer == Maybe.PRESENT) {
            1
        } else {
            0
        }
        return when (floor.size + extra) {
            0 -> 0
            1 -> 1
            2 -> 2
            3 -> 4
            4 -> 6
            5 -> 8
            6 -> 11
            else -> 14
        }
    }

    private fun tileLine(
        patternLine: PatternLine?,
        wallLine: WallLine,
        max: Int,
        trash: Trash,
        clearPatternLine: () -> Unit
    ) {
        if (patternLine != null && patternLine.count == max) {
            clearPatternLine()
            val index = getColumnFromColorRow(patternLine.color, max)
            wallLine.setColumn(index)
            trash.add(patternLine.color, max - 1)
            val lineScore = scoreLine(wallLine, index)
            val columnScore = scoreColumn(index, max - 1)
            score += if (lineScore == 0 && columnScore == 0) {
                1
            } else {
                lineScore + columnScore
            }
        }
    }

    private fun scoreLine(wallLine: WallLine, column: Int): Int {
        var sum = 0
        var index = column
        while (index != 0) {
            index -= 1
            if (wallLine.isColumnSet(index)) {
                sum += 1
            } else {
                index = 0
            }
        }
        index = column
        while (index != 4) {
            index += 1
            if (wallLine.isColumnSet(index)) {
                sum += 1
            } else {
                index = 4
            }
        }
        if (sum > 0) {
            sum += 1
        }
        return sum
    }

    private fun scoreColumn(column: Int, row: Int): Int {
        var sum = 0
        var index = row
        while (index != 0) {
            index -= 1
            if (wall.getLine(index).isColumnSet(column)) {
                sum += 1
            } else {
                index = 0
            }
        }
        index = row
        while (index != 4) {
            index += 1
            if (wall.getLine(index).isColumnSet(column)) {
                sum += 1
            } else {
                index = 4
            }
        }
        if (sum > 0) {
            sum += 1
        }
        return sum
    }

    private fun updateLine(
        line: PatternLine?,
        color: Color,
        tileCount: Int,
        max: Int,
        trash: Trash,
        saveLine: (PatternLine) -> Unit
    ) {
        val floorTileCount = line.add(color, tileCount, max, saveLine)
        updateFloor(color, floorTileCount, trash)
    }

    private fun updateFloor(color: Color, tileCount: Int, trash: Trash) {
        val used = floor.size + if (nextFirstPlayer == Maybe.PRESENT) { 1 } else { 0 }
        val remain = 7 - used
        val (toFloor, toTrash) = if (tileCount > remain) {
            remain to (tileCount - remain)
        } else {
            (tileCount) to 0
        }
        0.until(toFloor).forEach {
            floor.add(color)
        }
        trash.add(color, toTrash)
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
