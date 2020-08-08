package info.ditrapani

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.apache.logging.log4j.Logger

class AppTest : FreeSpec({
    "says hi" {
        val logger = mockk<Logger>()
        Server(logger).hi().shouldBe("hello")
    }
})
