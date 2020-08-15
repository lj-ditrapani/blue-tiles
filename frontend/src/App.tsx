import React from 'react'
import './App.css'

type AppProps = {
  hi: String
}

type AppState = {
  registered: Boolean,
  ready: Boolean
}

class App extends React.Component<AppProps, AppState> {
  constructor(props: AppProps) {
    super(props)
    this.state = {
      registered: false,
      ready: false,
    }
  }

  render() {
    if (this.state.registered) {
      return (
        <div className="App">
          <p> Registered </p>
          <p> this.props.hi </p>
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
