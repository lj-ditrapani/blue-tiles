export type Player = 'P1' | 'P2' | 'P3'
export type Color = 'WHITE' | 'RED' | 'BLUE' | 'GREEN' | 'BLACK'
export type Maybe = 'PRESENT' | 'MISSING'

export const parsePlayer = (str: string | null | undefined): Player => {
  const p = parsePlayerOrNull(str)
  if (p === null) {
    throw Error(`Illegal value for Player: ${str}`)
  } else {
    return p
  }
}

export const parsePlayerOrNull = (str: string | null | undefined): Player | null => {
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

export type Factory = {
  displays: Display[]
  leftovers: Leftovers
}

export type Display = {
  slot1: Color | null
  slot2: Color | null
  slot3: Color | null
  slot4: Color | null
}

export type Leftovers = {
  whites: number
  reds: number
  blues: number
  greens: number
  blacks: number
  nextFirstPlayer: Maybe
}

export type PlayRecord = {
  player: Player
  location: string
  color: Color
  tileCount: number
  nextFirstPlayer: Maybe
  moveTo: string
}
