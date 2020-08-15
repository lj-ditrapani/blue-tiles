package info.ditrapani.game

import info.ditrapani.Result
import info.ditrapani.Success
import info.ditrapani.board.Board
import info.ditrapani.board.newBoard
import info.ditrapani.factory.Factory
import info.ditrapani.factory.newFactoryFromSupply
import info.ditrapani.model.Color
import info.ditrapani.model.Play
import info.ditrapani.model.PlayRecord
import info.ditrapani.model.Player
import info.ditrapani.model.toJson
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj

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
    val board3: Board,
    var lastPlay: PlayRecord?,
    var winner: Player?
) {
    fun update(play: Play): Result {
        // do factory offer
        val (count, firstPlayer) = factory.update(play)
        val playRecord = PlayRecord(count, firstPlayer, play)
        val player = play.player
        when (player) {
            Player.P1 -> board1.update(playRecord)
            Player.P2 -> board2.update(playRecord)
            Player.P3 -> board3.update(playRecord)
        }
        lastPlay = playRecord

        if (factory.isEmpty()) {
            // do wall tilling and scoring
            // needs to be implemented

            // check if end of game
            // if not end of game
            // Prepare the next round
            // if end of game
            // add bonus score and mark complete
        }
        return Success
    }

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
                "board3" to board1.toJson(),
                "lastPlay" to lastPlay.toJson(),
                "winner" to winner?.name

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
        newBoard,
        null,
        null
    )
}
