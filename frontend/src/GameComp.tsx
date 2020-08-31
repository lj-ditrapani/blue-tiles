import React from 'react'
import { Game } from './models'
import { FactoryComp } from './FactoryComp'
import { BoardComp } from './BoardComp'
import Grid from '@material-ui/core/Grid'

type GameProps = {
  game: Game | null
}

export function GameComp(props: GameProps) {
  const game: Game | null = props.game
  if (game === null) {
    return <p>No game state yet</p>
  } else {
    return (
      <div>
        <p>
          You are player {game.requestingPerson} | currentFirstPlayer:{' '}
          {game.currentFirstPlayer} | currentPlayer: {game.currentPlayer} | supplyCount:{' '}
          {game.supplyCount} | trashCount: {game.trashCount} | winner:{' '}
          {String(game.winner)}
        </p>
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
      </div>
    )
  }
}
