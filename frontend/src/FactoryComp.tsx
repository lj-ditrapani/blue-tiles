import React from 'react'
import { Color, Display, Factory, Leftovers } from './models'
import { Button } from '@material-ui/core'
import Grid from '@material-ui/core/Grid'
import { red } from '@material-ui/core/colors'
// import { red, green, blue } from '@material-ui/core/colors'
import { createMuiTheme, ThemeProvider } from '@material-ui/core/styles'

type FactoryProps = {
  factory: Factory
}

export function FactoryComp(props: FactoryProps) {
  const factory: Factory = props.factory
  return (
    <div>
    <Grid container spacing={2}>
      <Grid item xs={6}>
        <Grid container spacing={2}>
          <Grid item xs={3}>
            Display 1: <DisplayComp display={factory.displays[0]} />
          </Grid>
          <Grid item xs={3}>
            Display 2: <DisplayComp display={factory.displays[1]} />
          </Grid>
          <Grid item xs={3}>
            Display 3: <DisplayComp display={factory.displays[2]} />
          </Grid>
          <Grid item xs={3}>
            Display 4: <DisplayComp display={factory.displays[3]} />
          </Grid>
        </Grid>
      </Grid>
      <Grid item xs={6}>
        <Grid container spacing={2}>
          <Grid item xs={3}>
            Display 5: <DisplayComp display={factory.displays[4]} />
          </Grid>
          <Grid item xs={3}>
            Display 6: <DisplayComp display={factory.displays[5]} />
          </Grid>
          <Grid item xs={3}>
            Display 7: <DisplayComp display={factory.displays[6]} />
          </Grid>
          <Grid item xs={3}>
          </Grid>
        </Grid>
      </Grid>
    </Grid>
    <LeftoversComp leftovers={factory.leftovers} />
    </div>
  )
}

type DisplayProps = {
  display: Display
}

function DisplayComp(props: DisplayProps) {
  const display = props.display
  return (
    <Grid container spacing={0}>
      <Grid item xs={6}>
        <TileComp color={display.slot1}/>
      </Grid>
      <Grid item xs={6}>
        <TileComp color={display.slot2}/>
      </Grid>
      <Grid item xs={6}>
        <TileComp color={display.slot3}/>
      </Grid>
      <Grid item xs={6}>
        <TileComp color={display.slot4}/>
      </Grid>
    </Grid>
  )
}

type LeftoverProps = {
  leftovers: Leftovers
}

function LeftoversComp(props: LeftoverProps) {
  const leftovers = props.leftovers
  return (
    <Grid container spacing={2}>
      <Grid item xs={2}>
        {leftovers.nextFirstPlayer === 'PRESENT' ? 'NextFirstPlayer' : ''}
      </Grid>
      <Grid item xs={2}>
        <TileSetComp color='WHITE' count={leftovers.whites} />
      </Grid>
      <Grid item xs={2}>
        <TileSetComp color='RED' count={leftovers.reds} />
      </Grid>
      <Grid item xs={2}>
        <TileSetComp color='BLUE' count={leftovers.blues} />
      </Grid>
      <Grid item xs={2}>
        <TileSetComp color='GREEN' count={leftovers.greens} />
      </Grid>
      <Grid item xs={2}>
        <TileSetComp color='BLACK' count={leftovers.blacks} />
      </Grid>
    </Grid>
  )
}

type TileProps = {
  color: Color | null
}

const redTheme = createMuiTheme({ palette: { primary: red } })
// const blueTheme = createMuiTheme({ palette: { primary: blue } })
// const greenTheme = createMuiTheme({ palette: { primary: green } })

function TileComp(props: TileProps)  {
  const color = props.color
  return (
    <ThemeProvider theme={redTheme}>
      <Button variant="contained" disabled={color === null}>{tileToLetter(color)}</Button>
    </ThemeProvider>
  )
}

type TileSetProps = {
  color: Color
  count: number
}

function TileSetComp(props: TileSetProps)  {
  const color = props.color
  const count = props.count
  return <Button variant="contained" disabled={count === 0}>{count} {color.toLowerCase() + 's'}</Button>
}

function tileToLetter(color: Color | null): string {
  switch(color) {
    case null:
      return 'E'
    case 'WHITE':
      return 'W'
    case 'RED':
      return 'R'
    case 'BLUE':
      return 'B'
    case 'GREEN':
      return 'G'
    case 'BLACK':
      return 'K'
  }
}
