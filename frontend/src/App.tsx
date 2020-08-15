import React from 'react'
import './App.css'

type AppProps = {
  hi: String
}

type AppState = {
  registered: Boolean
  ready: Boolean
  player: Player | undefined
  playerCount: number
  game: any
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
    // this.setState({ game: body })
  }

  render() {
    if (this.state.registered) {
      return (
        <div className="App">
          <p> Registered </p>
          <p> is ready? {this.state.ready} </p>
          <p> waiting for {3 - this.state.playerCount} players to join </p>
          <p> this.props.hi </p>
          <p> You are player {this.state.player} </p>
          <p> Game is {this.state.game} </p>
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

type Player = 'P1' | 'P2' | 'P3'

export default App
