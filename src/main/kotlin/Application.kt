package app.hononeko

import app.hononeko.app.hononeko.threadificatorr.adapter.MeilisearchThreadRepository
import io.ktor.server.application.*
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val meilisearchRepository = runBlocking { MeilisearchThreadRepository.init(environment.config) }
    configureHTTP()
    configureSecurity()
    configureMonitoring()
    configureSerialization()
    configureFrameworks()
    configureRouting()
}
