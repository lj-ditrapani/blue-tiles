package info.ditrapani.game

import info.ditrapani.Outcome
import info.ditrapani.board.Board
import info.ditrapani.board.newBoard
import info.ditrapani.factory.Factory
import info.ditrapani.factory.newFactoryFromSupply
import info.ditrapani.model.Color
import info.ditrapani.model.Maybe
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

    fun add(color: Color, count: Int) {
        when (color) {
            Color.WHITE -> whites += count
            Color.RED -> reds += count
            Color.BLUE -> blues += count
            Color.GREEN -> greens += count
            Color.BLACK -> blacks += count
        }
    }
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
    fun update(play: Play): Outcome {
        val validFactoryPlay = factory.isPlayValid(play)
        val validBoardPlay = when (play.player) {
            Player.P1 -> board1.isPlayValid(play)
            Player.P2 -> board2.isPlayValid(play)
            Player.P3 -> board3.isPlayValid(play)
        }
        return if (!(validFactoryPlay && validBoardPlay)) {
            Outcome.FAILURE
        } else {
            val (count, firstPlayer) = factory.update(play)
            val playRecord = PlayRecord(count, firstPlayer, play)
            val player = play.player
            when (player) {
                Player.P1 -> board1.update(playRecord, trash)
                Player.P2 -> board2.update(playRecord, trash)
                Player.P3 -> board3.update(playRecord, trash)
            }
            lastPlay = playRecord
            currentPlayer = currentPlayer.next()

            if (factory.isEmpty()) {
                board1.tileWall(trash)
                board2.tileWall(trash)
                board3.tileWall(trash)
                board1.cleanFloor(trash)
                board2.cleanFloor(trash)
                board3.cleanFloor(trash)
                if (listOf(board1, board2, board3).any { it.isGameOver() }) {
                    board1.addBonusScore()
                    board2.addBonusScore()
                    board3.addBonusScore()
                    val pair = listOf(
                        Player.P1 to board1.score,
                        Player.P2 to board2.score,
                        Player.P3 to board3.score
                    )
                        .maxBy { it.second }
                    winner = pair!!.first
                } else {
                    factory.reset(supply, trash)
                    var nextFirstPlayer = Player.P1
                    if (board2.nextFirstPlayer == Maybe.PRESENT) {
                        nextFirstPlayer = Player.P2
                    }
                    if (board3.nextFirstPlayer == Maybe.PRESENT) {
                        nextFirstPlayer = Player.P3
                    }
                    board1.nextFirstPlayer = Maybe.MISSING
                    board2.nextFirstPlayer = Maybe.MISSING
                    board3.nextFirstPlayer = Maybe.MISSING
                    currentFirstPlayer = nextFirstPlayer
                    currentPlayer = nextFirstPlayer
                }
            }
            Outcome.SUCCESS
        }
    }

    fun toJson(player: Player?): JsonObject =
        json {
            obj(
                "requestingPerson" to requestingPerson(player),
                "currentFirstPlayer" to currentFirstPlayer.toString(),
                "currentPlayer" to currentPlayer.toString(),
                "supplyCount" to supply.size,
                "trashCount" to trash.size(),
                "factory" to factory.toJson(),
                "board1" to board1.toJson(),
                "board2" to board2.toJson(),
                "board3" to board3.toJson(),
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
        newBoard(),
        newBoard(),
        newBoard(),
        null,
        null
    )
}
