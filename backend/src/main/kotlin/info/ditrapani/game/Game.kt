package info.ditrapani.game

import info.ditrapani.board.Board
import info.ditrapani.board.newBoard
import info.ditrapani.factory.Factory
import info.ditrapani.factory.newFactoryFromSupply
import info.ditrapani.model.Color
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

enum class Player {
    P1,
    P2,
    P3
}

fun requestingPerson(player: Player?): String =
    if (player == null) {
        "spectator"
    } else {
        player.name
    }

data class Trash(
    var whites: Int,
    var reds: Int,
    var blues: Int,
    var greens: Int,
    var blacks: Int
) {
    fun size(): Int = whites + reds + blues + greens + blacks

    fun toSupply(): Supply =
        mutableListOf<Color>().apply {
            addAll(MutableList(whites) { Color.WHITE })
            addAll(MutableList(reds) { Color.RED })
            addAll(MutableList(blues) { Color.BLUE })
            addAll(MutableList(greens) { Color.GREEN })
            addAll(MutableList(blacks) { Color.BLACK })
            shuffle()
        }

    fun isEmpty(): Boolean = toSupply().size == 0
}

data class Game(
    var currentFirstPlayer: Player,
    var currentPlayer: Player,
    val supply: Supply,
    val trash: Trash,
    val factory: Factory,
    val board1: Board,
    val board2: Board,
    val board3: Board
) {
    fun toJson(player: Player?): JsonObject =
        json {
            obj(
                "requestingPerson" to requestingPerson(player),
                "currentFirstPlayer" to currentPlayer.toString(),
                "currentPlayer" to currentPlayer.toString(),
                "factory" to factory.toJson(),
                "supplyCount" to supply.size,
                "trashCount" to trash.size(),
                "board1" to board1.toJson(),
                "board2" to board1.toJson(),
                "board3" to board1.toJson()
            )
        }
}

typealias Supply = MutableList<Color>

fun Supply.getTile(trash: Trash): Color? {
    if (isEmpty()) {
        if (trash.isEmpty()) {
            return null
        }
        addAll(trash.toSupply())
    }
    return removeAt(0)
}

fun newSupply(): Supply =
    Trash(20, 20, 20, 20, 20).toSupply()

fun newGame(): Game {
    val supply = newSupply()
    val trash = Trash(0, 0, 0, 0, 0)
    val factory = newFactoryFromSupply(supply, trash)
    return Game(
        Player.P1,
        Player.P1,
        supply,
        trash,
        factory,
        newBoard,
        newBoard,
        newBoard
    )
}
