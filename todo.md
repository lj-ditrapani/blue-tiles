- implement server-side play
    - Once factory is empty
        - score
        - deal penalties (then put floor tiles in trash)
        - on game end
            - compute bonus points
Anti-cheat/correctness:
- You can chose to moveTo a patternLine with a color that has already been scored/tiled in the corresponding line of the wall.
- bug: if your floor is full BEFORE taking from center factory leftovers
  when you take from the center (first time in round)
  the nextFirstPlayer token "goes in the trash"
- make floor a smarter class type
    - call add(color, trash) or addNextFirstPlayer(trash)
    - puts extras in trash automatically
    - replaces a tile with nextFirstPlayer when full and puts tile in trash
- dockerize
- detect end-game state and set winner
