package app.hononeko

import app.hononeko.app.hononeko.threadificatorr.adapter.MeilisearchThreadRepository
import app.hononeko.app.hononeko.threadificatorr.core.UnifiedNotification
import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.core.spec.style.StringSpec
import io.kotest.engine.runBlocking
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.server.config.*
import io.ktor.server.config.yaml.*
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

class MeilisearchRepositoryTest : StringSpec({
    val logger = KotlinLogging.logger {}

    val config = YamlConfig("test-config.yaml")!!

    val meilisearchContainer =
        GenericContainer(DockerImageName.parse("getmeili/meilisearch:v1.7")).withExposedPorts(7700)
            .withEnv("MEILI_MASTER_KEY", config.property("meilisearch.masterKey").getString())

    lateinit var repository: MeilisearchThreadRepository

    val client = HttpClient(CIO) {
        defaultRequest {
            url("http://${meilisearchContainer.host}:${meilisearchContainer.getMappedPort(7700)}")
        }
    }

    beforeSpec {
        meilisearchContainer.start()
        val testConfig = config.mergeWith(
            MapApplicationConfig(
                "meilisearch.host" to "http://${meilisearchContainer.host}:${meilisearchContainer.getMappedPort(7700)}"
            )
        )

        repository = runBlocking {
            MeilisearchThreadRepository.init(
                config = testConfig, client = client
            ) as MeilisearchThreadRepository
        }
    }

    afterSpec {
        meilisearchContainer.stop()
    }

    "Notification should be saved" {
        val notification = UnifiedNotification(
            mediaId = "123",
            mediaName = "Star Wars",
            notificationText = "Star Wars was added to Server!",
        )
        runBlocking { repository.add(notification) }
        val result = repository.get(notification.mediaName)
        logger.info { result }
    }
})
