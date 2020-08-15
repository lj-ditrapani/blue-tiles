import React from 'react'
import './App.css'

type AppProps = {
  hi: String
}

type Game = {
  requestingPerson: Player | null
  currentFirstPlayer: Player
  currentPlayer: Player
  // "factory" to factory.toJson(),
  supplyCount: number
  trashCount: number
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
    this.setState({
      game: {
        requestingPerson: parsePlayerOrNull(body.requestingPerson),
        currentFirstPlayer: parsePlayer(body.currentFirstPlayer),
        currentPlayer: parsePlayer(body.currentPlayer),
        supplyCount: body.supplyCount,
        trashCount: body.trashCount,
        winner: parsePlayerOrNull(body.winner),
      },
    })
  }

  render() {
    if (this.state.registered) {
      return (
        <div className="App">
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

type Player = 'P1' | 'P2' | 'P3'

export default App

const parsePlayer = (str: string | null | undefined): Player => {
  const p = parsePlayerOrNull(str)
  if (p === null) {
    throw Error('Player is null')
  } else {
    return p
  }
}

const parsePlayerOrNull = (str: string | null | undefined): Player | null => {
  if (str === 'P1') {
    return 'P1'
  } else if (str === 'P2') {
    return 'P2'
  } else if (str === 'P3') {
    return 'P3'
  } else {
    return null
  }
}
