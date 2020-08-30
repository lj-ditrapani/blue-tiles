import React from 'react'
import './App.css'
import { Game, Player, parsePlayer, parsePlayerOrNull } from './models'
import { GameComp } from './GameComp'

type AppProps = {
  hi: String
}

type AppState = {
  registered: boolean
  ready: boolean
  player: Player | undefined
  playerCount: number
  game: Game | null
}

class App extends React.Component<AppProps, AppState> {
  constructor(props: AppProps) {
    super(props)
    this.state = {
      registered: false,
      ready: false,
      player: undefined,
      playerCount: 0,
      game: null,
    }
  }

  componentDidMount() {
    this.setup()
  }

  async setup() {
    await this.registerUser()
    await this.waitForReady()
    await this.gameLoop()
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
          <div>
            <button>Play</button>
          </div>
          <p> Registered </p>
          <p> is ready? {String(this.state.ready)} </p>
          <p> waiting for {3 - this.state.playerCount} players to join </p>
          <p> this.props.hi </p>
          <p> You are player {this.state.player} </p>
          <GameComp game={this.state.game} />
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
