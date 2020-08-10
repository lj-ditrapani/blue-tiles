package info.ditrapani.game

import info.ditrapani.model.Color
import info.ditrapani.factory.Factory
import info.ditrapani.factory.newFactoryFromSupply
import info.ditrapani.board.Board
import info.ditrapani.board.newBoard
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

data class Trash(
    val whites: Int,
    val reds: Int,
    val blues: Int,
    val greens: Int,
    val blacks: Int
) {
    fun size(): Int = whites + reds + blues + greens + blacks

    fun toSupply(): List<Color> =
        (
            List(whites) { Color.WHITE } +
                List(reds) { Color.RED } +
                List(blues) { Color.BLUE } +
                List(greens) { Color.GREEN } +
                List(blacks) { Color.BLACK }
        )
        .shuffled()
}


data class Game(
    val currentFirstPlayer: Player,
    val currentPlayer: Player,
    val supply: List<Color>,
    val trash: Trash,
    val factory: Factory,
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

fun newSupply(): List<Color> =
    Trash(20, 20, 20, 20, 20).toSupply()

fun newGame(): Game {
    val supply = newSupply()
    val factory = newFactoryFromSupply(supply)
    return Game(
        Player.P1,
        Player.P1,
        supply,
        Trash(0, 0, 0, 0, 0),
        factory,
        newBoard,
        newBoard,
        newBoard
    )
}
