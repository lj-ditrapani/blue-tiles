import React from 'react'
import { Game } from './models'

type GameProps = {
  game: Game | null
}

export class GameComp extends React.Component<GameProps> {
  render() {
    const game: Game | null = this.props.game
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
          <p>factory: {game.factory}</p>
          <p>winner: {String(game.winner)}</p>
        </div>
      )
    }
  }
}
