import React from 'react'

import { Board, Player, PatternLine, WallLine, Maybe } from './models'
import Grid from '@material-ui/core/Grid'
import { colorToBgHex, colorToFgHex, tileToLetter } from './color'

type BoardProps = {
  board: Board
  player: Player
}

export function BoardComp(props: BoardProps) {
  const board = props.board
  return (
    <div
      style={{
        border: '3px solid black',
        backgroundColor: '#bbeeff',
        padding: '2px',
        margin: '2px',
      }}
    >
      <Grid container spacing={0}>
        <Grid item xs={12}>
          {props.player} board | score = {board.score} |
          {board.nextFirstPlayer === 'PRESENT'
            ? ' Is next first player'
            : ' Not next first player'}
        </Grid>
        <BoardLine
          patternLine={board.line1}
          wallLine={board.wall.line1}
          patternLineSize={1}
        />
        <BoardLine
          patternLine={board.line2}
          wallLine={board.wall.line2}
          patternLineSize={2}
        />
        <BoardLine
          patternLine={board.line3}
          wallLine={board.wall.line3}
          patternLineSize={3}
        />
        <BoardLine
          patternLine={board.line4}
          wallLine={board.wall.line4}
          patternLineSize={4}
        />
        <BoardLine
          patternLine={board.line5}
          wallLine={board.wall.line5}
          patternLineSize={5}
        />
        <Grid item xs={12}>
          Floor: {board.floor.join(' ')}
        </Grid>
      </Grid>
    </div>
  )
}

type BoardLineProps = {
  patternLine: PatternLine | null
  wallLine: WallLine
  patternLineSize: number
}

const baseColors = ['#ffffff', '#ff4040', '#4040ff', '#80E080', '#000000']

export function BoardLine(props: BoardLineProps) {
  const patternLine = props.patternLine
  const wallLine = props.wallLine
  const patternLineSize = props.patternLineSize
  const colors = baseColors.slice()
  colors.push.apply(colors, colors.splice(0, 6 - patternLineSize))
  return (
    <Grid item xs={12}>
      <div>
        <Grid container spacing={0}>
          <Grid item xs={2}>
            {generatePatternLineContent(patternLine, patternLineSize)}
          </Grid>
          <Grid item xs={2}>
            {wallSpan(wallLine.c1, colors[0])}
          </Grid>
          <Grid item xs={2}>
            {wallSpan(wallLine.c2, colors[1])}
          </Grid>
          <Grid item xs={2}>
            {wallSpan(wallLine.c3, colors[2])}
          </Grid>
          <Grid item xs={2}>
            {wallSpan(wallLine.c4, colors[3])}
          </Grid>
          <Grid item xs={2}>
            {wallSpan(wallLine.c5, colors[4])}
          </Grid>
        </Grid>
      </div>
    </Grid>
  )
}

function wallSpan(maybe: Maybe, hexColor: string) {
  return (
    <div
      style={{
        backgroundColor: maybe === 'PRESENT' ? hexColor : '#a5a5a5',
        border: `3px solid ${hexColor}`,
      }}
    >
      {maybe === 'MISSING' ? 'E' : ':)'}
    </div>
  )
}

function generatePatternLineContent(
  patternLine: PatternLine | null,
  patternLineSize: number
) {
  if (patternLine === null) {
    return <div style={{ backgroundColor: '#a5a5a5' }}> 0/{patternLineSize} E </div>
  } else {
    return (
      <div
        style={{
          backgroundColor: colorToBgHex(patternLine.color),
          color: colorToFgHex(patternLine.color),
        }}
      >
        {patternLine.count}/{patternLineSize} {tileToLetter(patternLine.color)}
      </div>
    )
  }
}
