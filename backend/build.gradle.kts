import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm").version("1.3.72")
    application
    jacoco
    id("org.jlleitschuh.gradle.ktlint").version("9.2.1")
    id("com.github.ben-manes.versions").version("0.28.0")
}

repositories {
    jcenter()
}

dependencies {
    val log4jVersion = "2.13.1"
    val vertxVersion = "3.9.0"
    val kotestVersion = "4.1.3"

    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    implementation("io.vertx:vertx-lang-kotlin:$vertxVersion")
    implementation("io.vertx:vertx-lang-kotlin-coroutines:$vertxVersion")
    implementation("io.vertx:vertx-web:$vertxVersion")
    implementation("io.vertx:vertx-web-client:$vertxVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    
    testImplementation("io.mockk:mockk:1.10.0")
    testImplementation("io.vertx:vertx-web-client:$vertxVersion")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
}

application {
    mainClassName = "info.ditrapani.AppKt"
}

ktlint {
    version.set("0.36.0")
    enableExperimentalRules.set(true)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.test {
    testLogging {
        events("passed", "started", "failed", "skipped")
    }
    useJUnitPlatform()
}
