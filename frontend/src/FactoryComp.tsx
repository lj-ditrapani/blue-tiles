import React from 'react'
import { Display, Factory, Leftovers } from './models'

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
      <button>{display.slot1}</button>
      <button>{display.slot2}</button>
      <button>{display.slot3}</button>
      <button>{display.slot4}</button>
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
      <button>{leftovers.whites} whites</button>
      <button>{leftovers.reds} reds</button>
      <button>{leftovers.blues} blues</button>
      <button>{leftovers.greens} greens</button>
      <button>{leftovers.blacks} blacks</button>
    </span>
  )
}
