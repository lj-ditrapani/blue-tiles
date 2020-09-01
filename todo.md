- implement server-side play
    - Once factory is empty
        - score (then put extra tiles in trash)
        - deal penalties (then put floor tiles in trash)
        - on game end
            - compute bonus points
- bug: if your floor is full BEFORE taking from center factory leftovers
  when you take from the center (first time in round)
  the nextFirstPlayer token "goes in the trash"
- dockerize
- detect end-game state and set winner
