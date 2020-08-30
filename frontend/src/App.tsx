import React from 'react'
import './App.css'
import { Player, parsePlayer, parsePlayerOrNull, Factory} from './models'

type AppProps = {
  hi: String
}

type Game = {
  requestingPerson: Player | null
  currentFirstPlayer: Player
  currentPlayer: Player
  supplyCount: number
  trashCount: number
  factory: Factory
  /*
  "board1" to board1.toJson(),
  "board2" to board1.toJson(),
  "board3" to board1.toJson(),
  "lastPlay" to lastPlay.toJson(),
  */
  winner: Player | null
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
          {this.renderGame(this.state.game)}
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

  renderGame(game: Game | null) {
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
          <p>winner: {String(game.winner)}</p>
        </div>
      )
    }
  }
}

export default App
