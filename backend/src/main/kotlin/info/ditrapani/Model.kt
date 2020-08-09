package info.ditrapani

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

sealed class FirstPlayer
object FirstPlayerAtFactory : FirstPlayer()
data class FirstPlayerIs(val player: Player) : FirstPlayer()

data class Display(
    val slot1: Color?,
    val slot2: Color?,
    val slot3: Color?,
    val slot4: Color?
)

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
    val firstPlayer: Maybe
)

data class Factory(val displays: List<Display>, val leftovers: Leftovers)

data class Trash(
    val whites: Int,
    val reds: Int,
    val blues: Int,
    val greens: Int,
    val blacks: Int
)

data class StorageRow(val color: Color, val count: Int)

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
    val grid: Grid
)

data class Game(
    val currentFirstPlayer: Player,
    val nextFirstPlayer: FirstPlayer,
    val currentPlayer: Player,
    val factory: Factory,
    val supply: List<Color>,
    val trash: Trash,
    val board1: Board,
    val board2: Board,
    val board3: Board
)
