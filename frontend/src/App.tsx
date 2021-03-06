import React from 'react'
import './App.css'
import {
  Color,
  Game,
  Location,
  MoveTo,
  Player,
  parsePlayer,
  parsePlayerOrNull,
} from './models'
import { GameComp } from './GameComp'
import CssBaseline from '@material-ui/core/CssBaseline'

type AppState = {
  registered: boolean
  ready: boolean
  player: Player | undefined
  playerCount: number
  game: Game | null
  selectedLocation: Location | null
  selectedColor: Color | null
}

class App extends React.Component<{}, AppState> {
  constructor() {
    super({})
    this.state = {
      registered: false,
      ready: false,
      player: undefined,
      playerCount: 0,
      game: null,
      selectedLocation: null,
      selectedColor: null,
    }
  }

  componentDidMount() {
    this.setup()
  }

  onTileSelect = (location: Location, color: Color) => {
    const game = this.state.game
    if (
      game !== null &&
      game.requestingPerson !== null &&
      game.requestingPerson === game.currentPlayer
    ) {
      this.setState({
        selectedLocation: location,
        selectedColor: color,
      })
    }
  }

  onLineSelect = async (boardNumber: Player, moveTo: MoveTo) => {
    const game = this.state.game
    const selectedLocation = this.state.selectedLocation
    const selectedColor = this.state.selectedColor
    if (
      game !== null &&
      game.requestingPerson !== null &&
      game.requestingPerson === game.currentPlayer &&
      game.requestingPerson === boardNumber &&
      selectedLocation !== null &&
      selectedColor !== null
    ) {
      await fetch(
        `/play/${this.state.selectedLocation}/${this.state.selectedColor}/${moveTo}`,
        { method: 'POST' }
      )
      this.gameLoop()
    }
  }

  async setup() {
    await this.registerUser()
    await this.waitForReady()
  }

  async registerUser() {
    const result = await fetch('/register', { method: 'POST' })
    const body = await result.json()
    console.log('register success')
    console.log(body)
    if (body.status === 'registered') {
      this.setState({
        player: body.player,
        registered: true,
      })
    }
  }

  async waitForReady() {
    const result = await fetch('/ready')
    const body = await result.json()
    console.log(body)
    if (body.ready) {
      this.setState({
        ready: true,
        playerCount: body.count,
      })
      this.gameLoop()
    } else {
      this.setState({ playerCount: body.count })
      setTimeout(() => this.waitForReady(), 500)
    }
  }

  async gameLoop() {
    const result = await fetch('/status')
    const body = await result.json()
    console.log('called status:')
    console.log(body)
    const currentPlayer = parsePlayer(body.currentPlayer)
    this.setState({
      selectedLocation: null,
      selectedColor: null,
      game: {
        requestingPerson: parsePlayerOrNull(body.requestingPerson),
        currentFirstPlayer: parsePlayer(body.currentFirstPlayer),
        currentPlayer: currentPlayer,
        supplyCount: body.supplyCount,
        trashCount: body.trashCount,
        factory: body.factory,
        board1: body.board1,
        board2: body.board2,
        board3: body.board3,
        lastPlay: body.lastPlay,
        winner: parsePlayerOrNull(body.winner),
      },
    })
    if (currentPlayer === this.state.player) {
      console.log('Your turn!')
    } else {
      setTimeout(() => this.gameLoop(), 500)
    }
  }

  render() {
    if (this.state.registered) {
      return (
        <div className="App">
          <CssBaseline />
          <p>
            {this.state.ready
              ? ''
              : `Waiting for ${
                  3 - this.state.playerCount
                } players to join.  You are player ${this.state.player}`}
          </p>
          <GameComp
            game={this.state.game}
            onTileSelect={this.onTileSelect}
            onLineSelect={this.onLineSelect}
          />
          <p>
            {this.state.selectedLocation !== null && this.state.selectedColor !== null
              ? `You selected ${this.state.selectedColor} from ${this.state.selectedLocation}.` +
                'Now select a pattern line on your board to place the tile(s) in.'
              : ''}
          </p>
        </div>
      )
    } else {
      return (
        <div className="App">
          <p> Registering user </p>
        </div>
      )
    }
  }
}

export default App
