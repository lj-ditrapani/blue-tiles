import React from 'react'
import { Display, Factory, Leftovers } from './models'
import { Button } from '@material-ui/core'

type FactoryProps = {
  factory: Factory
}

export function FactoryComp(props: FactoryProps) {
  const factory: Factory = props.factory
  return (
    <div>
      <p>
        Display 1: <DisplayComp display={factory.displays[0]} />
      </p>
      <p>
        Display 2: <DisplayComp display={factory.displays[1]} />
      </p>
      <p>
        Display 3: <DisplayComp display={factory.displays[2]} />
      </p>
      <p>
        Display 4: <DisplayComp display={factory.displays[3]} />
      </p>
      <p>
        Display 5: <DisplayComp display={factory.displays[4]} />
      </p>
      <p>
        Display 6: <DisplayComp display={factory.displays[5]} />
      </p>
      <p>
        Display 7: <DisplayComp display={factory.displays[6]} />
      </p>
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
    <span>
      <Button>{display.slot1}</Button>
      <Button>{display.slot2}</Button>
      <Button>{display.slot3}</Button>
      <Button>{display.slot4}</Button>
    </span>
  )
}

type LeftoverProps = {
  leftovers: Leftovers
}

function LeftoversComp(props: LeftoverProps) {
  const leftovers = props.leftovers
  return (
    <span>
      {leftovers.nextFirstPlayer === 'PRESENT' ? 'NextFirstPlayer' : ''}
      <Button>{leftovers.whites} whites</Button>
      <Button>{leftovers.reds} reds</Button>
      <Button>{leftovers.blues} blues</Button>
      <Button>{leftovers.greens} greens</Button>
      <Button>{leftovers.blacks} blacks</Button>
    </span>
  )
}
