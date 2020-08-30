import React from 'react'
import { Display, Factory, Leftovers } from './models'
import { Button } from '@material-ui/core'
import Grid from '@material-ui/core/Grid'

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
        <Button>{display.slot1}</Button>
      </Grid>
      <Grid item xs={6}>
        <Button>{display.slot2}</Button>
      </Grid>
      <Grid item xs={6}>
        <Button>{display.slot3}</Button>
      </Grid>
      <Grid item xs={6}>
        <Button>{display.slot4}</Button>
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
        <Button>{leftovers.whites} whites</Button>
      </Grid>
      <Grid item xs={2}>
        <Button>{leftovers.reds} reds</Button>
      </Grid>
      <Grid item xs={2}>
        <Button>{leftovers.blues} blues</Button>
      </Grid>
      <Grid item xs={2}>
        <Button>{leftovers.greens} greens</Button>
      </Grid>
      <Grid item xs={2}>
        <Button>{leftovers.blacks} blacks</Button>
      </Grid>
    </Grid>
  )
}
