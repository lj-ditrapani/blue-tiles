Develop
-------

Develop dependecies: [java 11.0.8](https://sdkman.io/)

Format code

    ./gradlew ktlintFormat

Run unit tests

    ./gradlew test

Run test coverage report

    ./gradlew test jacocoTestReport
    firefox build/reports/jacoco/test/html/index.html

Lint, test, run

    ./gradlew ktlintFormat test run

Dev build

    ./gradlew build

Run installed

    ./gradlew installDist
    ./build/install/blue/bin/blue


API
---

```
post register
    Creates session cookie.
    Register's player number in session.
get ready
    Returns false if still waiting for 3 players.
    Returns true once all 3 players have joined.
get status
    Returns the complete current state of the game.
post play
    Only legal for current player to post play.
    Allows player to send chosen play.
```
