val koin_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val kotest_version: String by project

plugins {
    kotlin("jvm") version "2.2.0"
    id("io.ktor.plugin") version "3.1.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.0"
    id("com.figure.gradle.semver-plugin") version "1.10.0"
}

group = "app.hononeko"
version = semver.version

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("io.ktor:ktor-server-cors")
    implementation("io.ktor:ktor-server-forwarded-header")
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-openapi")
    implementation("com.ucasoft.ktor:ktor-simple-cache:0.53.4")
    implementation("com.ucasoft.ktor:ktor-simple-redis-cache:0.53.4")
    implementation("io.ktor:ktor-server-swagger")
    
    implementation("io.ktor:ktor-server-request-validation")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("dev.hayden:khealth:3.0.2")
    implementation("io.ktor:ktor-server-metrics")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    implementation("io.ktor:ktor-server-netty")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")
    implementation(platform("io.arrow-kt:arrow-stack:2.1.0"))
    implementation("io.arrow-kt:arrow-core")
    implementation("io.arrow-kt:arrow-fx-coroutines")
    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-client-cio")
    implementation("io.ktor:ktor-client-content-negotiation")
    // logging
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.7")
    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.18")
    implementation("io.ktor:ktor-client-logging:3.0.0-beta-1")
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.kotest:kotest-assertions-core:$kotest_version")
    testImplementation("io.kotest:kotest-property:$kotest_version")
    testImplementation("io.kotest:kotest-runner-junit5:$kotest_version")
    testImplementation("org.testcontainers:testcontainers:1.19.8")
    testImplementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.17.1")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1")
}

semver {
    initialVersion("0.1.0")
    versionModifier { nextMinor() }
    tagPrefix("")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}