import React from 'react'

import { Board, Player, PatternLine, WallLine } from './models'
import Grid from '@material-ui/core/Grid'
import { colorToBgHex, colorToFgHex } from './color'

type BoardProps = {
  board: Board
  player: Player
}

export function BoardComp(props: BoardProps) {
  const board = props.board
  return (
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
  )
}

type BoardLineProps = {
  patternLine: PatternLine | null
  wallLine: WallLine
  patternLineSize: number
}

export function BoardLine(props: BoardLineProps) {
  const patternLine = props.patternLine
  const wallLine = props.wallLine
  const patternLineSize = props.patternLineSize
  return (
    <Grid item xs={12}>
      <div>
        <Grid container spacing={0}>
          <Grid item xs={2}>
            {generatePatternLineContent(patternLine, patternLineSize)}
          </Grid>
          <Grid item xs={2}>
            {wallLine.c1}
          </Grid>
          <Grid item xs={2}>
            {wallLine.c2}
          </Grid>
          <Grid item xs={2}>
            {wallLine.c3}
          </Grid>
          <Grid item xs={2}>
            {wallLine.c4}
          </Grid>
          <Grid item xs={2}>
            {wallLine.c5}
          </Grid>
        </Grid>
      </div>
    </Grid>
  )
}

function generatePatternLineContent(
  patternLine: PatternLine | null,
  patternLineSize: number
) {
  if (patternLine === null) {
    return <span style={{ backgroundColor: 'grey' }}> 0/{patternLineSize} Empty </span>
  } else {
    return (
      <span
        style={{
          backgroundColor: colorToBgHex(patternLine.color),
          color: colorToFgHex(patternLine.color),
        }}
      >
        {patternLine.count}/{patternLineSize} {patternLine.color.toLowerCase()}
      </span>
    )
  }
}
