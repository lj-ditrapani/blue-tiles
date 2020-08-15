package info.ditrapani

import info.ditrapani.model.Player
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class PlayerCounterTest : FreeSpec({
    "getAndInc increments player count and returns Players up to 3" {
        val playerCounter = PlayerCounter()
        playerCounter.count().shouldBe(0)
        playerCounter.isReady().shouldBe(false)
        playerCounter.getAndInc().shouldBe(Player.P1)
        playerCounter.count().shouldBe(1)
        playerCounter.isReady().shouldBe(false)
        playerCounter.getAndInc().shouldBe(Player.P2)
        playerCounter.count().shouldBe(2)
        playerCounter.isReady().shouldBe(false)
        playerCounter.getAndInc().shouldBe(Player.P3)
        playerCounter.count().shouldBe(3)
        playerCounter.isReady().shouldBe(true)
        playerCounter.getAndInc().shouldBe(null)
        playerCounter.count().shouldBe(3)
        playerCounter.isReady().shouldBe(true)
        playerCounter.getAndInc().shouldBe(null)
        playerCounter.count().shouldBe(3)
        playerCounter.isReady().shouldBe(true)
    }
})
