Build & run dependecies: [docker](https://www.docker.com/)


Build
-----

    cd backend
    sh build.sh


Run
---

    sh docker-run.sh


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
