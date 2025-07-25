val koin_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.1.20"
    id("io.ktor.plugin") version "3.1.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.20"
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
    implementation("io.ktor:ktor-server-csrf")
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
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

semver {
    initialVersion("0.1.0")
    versionModifier { nextMinor() }
    tagPrefix("")
}