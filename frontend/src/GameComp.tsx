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
        <p>requestingPerson: {game.requestingPerson}</p>
        <p>currentFirstPlayer: {game.currentFirstPlayer}</p>
        <p>currentPlayer: {game.currentPlayer}</p>
        <p>supplyCount: {game.supplyCount}</p>
        <p>trashCount: {game.trashCount}</p>
        <FactoryComp factory={game.factory} />
        <p>winner: {String(game.winner)}</p>
      </div>
    )
  }
}
