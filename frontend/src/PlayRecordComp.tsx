import React from 'react'
import { PlayRecord } from './models'

type PlayRecordProps = {
  play: PlayRecord | null
}

export function PlayRecordComp(props: PlayRecordProps) {
  const play = props.play
  return play === null ? (
    <p></p>
  ) : (
    <p>
      The last play: {play.player} took {play.tileCount} {play.color.toLowerCase()} tiles
      from {play.location} and placed them in {play.moveTo}.{' '}
      {play.nextFirstPlayer === 'PRESENT' ? ' They took the nextFirstPlayer token.' : ''}
    </p>
  )
}
