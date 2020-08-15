package info.ditrapani

import info.ditrapani.model.Player

class PlayerCounter {
    private var i = 0

    fun isReady(): Boolean = i == 3

    fun count(): Int = i

    fun getAndInc(): Player? =
        if (i == 3) {
            null
        } else {
            i += 1
            Player.valueOf("P$i")
        }
}
