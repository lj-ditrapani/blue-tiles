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

export type Game = {
  requestingPerson: Player | null
  currentFirstPlayer: Player
  currentPlayer: Player
  supplyCount: number
  trashCount: number
  factory: Factory
  board1: Board
  board2: Board
  board3: Board
  lastPlay: PlayRecord | null
  winner: Player | null
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

export type Board = {
  score: number
  line1: PatternLine | null
  line2: PatternLine | null
  line3: PatternLine | null
  line4: PatternLine | null
  line5: PatternLine | null
  wall: Wall
  nextFirstPlayer: Maybe
  floor: Color[]
}

export type PatternLine = {
  color: Color
  count: number
}

export type Wall = {
  line1: WallLine
  line2: WallLine
  line3: WallLine
  line4: WallLine
  line5: WallLine
}

export type WallLine = {
  c1: Maybe
  c2: Maybe
  c3: Maybe
  c4: Maybe
  c5: Maybe
}

export type DisplayNumber = 1 | 2 | 3 | 4 | 5 | 6 | 7

export class DisplayLocation {
  constructor(public readonly displayNumber: DisplayNumber) {}

  toString() {
    return `display${this.displayNumber}`
  }
}
export type Location = 'leftovers' | DisplayLocation
