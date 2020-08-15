package info.ditrapani.board

import info.ditrapani.model.Maybe

data class WallLine(
    var c1: Maybe,
    var c2: Maybe,
    var c3: Maybe,
    var c4: Maybe,
    var c5: Maybe
)

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
    fun isGameOver(): Boolean = true
}

fun newWall(): Wall =
    Wall(
        newGridRow(),
        newGridRow(),
        newGridRow(),
        newGridRow(),
        newGridRow()
    )
