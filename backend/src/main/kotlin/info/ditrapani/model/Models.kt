package info.ditrapani.model

enum class Color {
    WHITE,
    RED,
    BLUE,
    GREEN,
    BLACK
}

fun parseColor(str: String?): Color? =
    str?.let { notNull ->
        try {
            Color.valueOf(notNull)
        } catch (error: IllegalArgumentException) {
            null
        }
    }

enum class Maybe {
    PRESENT,
    MISSING
}

enum class Player {
    P1 {
        override fun next(): Player = Player.P2
    },
    P2 {
        override fun next(): Player = Player.P3
    },
    P3 {
        override fun next(): Player = Player.P1
    };

    abstract fun next(): Player
}
