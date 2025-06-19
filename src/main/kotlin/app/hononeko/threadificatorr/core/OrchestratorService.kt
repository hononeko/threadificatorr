package app.hononeko.app.hononeko.threadificatorr.core

import io.github.oshai.kotlinlogging.KotlinLogging

/**
 * The core business logic of the app
 */
class OrchestratorService {
    private val log = KotlinLogging.logger {}
    fun processWebhook(payload: String, source: WebhookSource) {
        log.info { "Processing webhook $payload from ${source.app}" }
    }
}

/**
 * Defines a fixed set of webhook producers
 */
enum class WebhookSource(val app: String) {
    RADARR("radarr"),
    SONARR("sonarr"),
    PROWLARR("prowlarr"),
}