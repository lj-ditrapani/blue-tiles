- dockerize
- bug: if your floor is full BEFORE taking from center factory leftovers
  when you take from the center (first time in round)
  the nextFirstPlayer token "goes in the trash"
- make floor a smarter class type
    - call add(color, trash) or addNextFirstPlayer(trash)
    - puts extras in trash automatically
    - replaces a tile with nextFirstPlayer when full and puts tile in trash
