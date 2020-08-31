import { Color } from './models'

export function colorToBgHex(color: Color | null): string {
  switch (color) {
    case null:
      return '#a5a5a5'
    case 'WHITE':
      return '#ffffff'
    case 'RED':
      return '#ff4040'
    case 'BLUE':
      return '#4040ff'
    case 'GREEN':
      return '#80E080'
    case 'BLACK':
      return '#000000'
  }
}

export const colorToFgHex = (color: Color | null): string =>
  color === 'BLACK' ? '#ffffff' : '#000000'
