import React from 'react'
import { Game } from './models'
import { FactoryComp } from './FactoryComp'

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
          requestingPerson: {game.requestingPerson} | currentFirstPlayer:{' '}
          {game.currentFirstPlayer} | currentPlayer: {game.currentPlayer} | supplyCount:{' '}
          {game.supplyCount} | trashCount: {game.trashCount} | winner:{' '}
          {String(game.winner)}
        </p>
        <FactoryComp factory={game.factory} />
      </div>
    )
  }
}
