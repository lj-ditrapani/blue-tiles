package info.ditrapani

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class AppTest : FreeSpec({
    "says hi" {
        Server().hi().shouldBe("hello")
    }
})
