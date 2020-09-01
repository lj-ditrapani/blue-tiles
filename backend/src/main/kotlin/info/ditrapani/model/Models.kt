package info.ditrapani.model

enum class Color(val index: Int) {
    WHITE(0),
    RED(1),
    BLUE(2),
    GREEN(3),
    BLACK(4)
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
