import React from 'react'
import { Game } from './models'
import { FactoryComp } from './FactoryComp'
import { BoardComp } from './BoardComp'
import { PlayRecordComp } from './PlayRecordComp'
import Grid from '@material-ui/core/Grid'

type GameProps = {
  game: Game | null
}

export function GameComp(props: GameProps) {
  const game: Game | null = props.game
  if (game === null) {
    return <p></p>
  } else {
    return (
      <div>
        <Grid container spacing={0}>
          <Grid item xs={2}>
            You are player {game.requestingPerson}
          </Grid>
          <Grid item xs={2}>
            currentFirstPlayer: {game.currentFirstPlayer}
          </Grid>
          <Grid item xs={2}>
            currentPlayer: {game.currentPlayer}
          </Grid>
          <Grid item xs={2}>
            supplyCount: {game.supplyCount}
          </Grid>
          <Grid item xs={2}>
            trashCount: {game.trashCount}
          </Grid>
          <Grid item xs={2}>
            winner: {game.winner === null ? 'None yet' : game.winner}
          </Grid>
        </Grid>
        <FactoryComp factory={game.factory} />
        <Grid container spacing={1}>
          <Grid item xs={4}>
            <BoardComp board={game.board1} player="P1" />
          </Grid>
          <Grid item xs={4}>
            <BoardComp board={game.board2} player="P2" />
          </Grid>
          <Grid item xs={4}>
            <BoardComp board={game.board3} player="P3" />
          </Grid>
        </Grid>
        <PlayRecordComp play={game.lastPlay} />
      </div>
    )
  }
}
