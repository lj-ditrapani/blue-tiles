package info.ditrapani.board

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
}

fun newWall(): Wall =
    Wall(
        newGridRow(),
        newGridRow(),
        newGridRow(),
        newGridRow(),
        newGridRow()
    )
