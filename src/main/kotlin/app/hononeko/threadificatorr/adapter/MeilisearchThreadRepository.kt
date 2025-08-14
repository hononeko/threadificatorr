package app.hononeko.app.hononeko.threadificatorr.adapter

import app.hononeko.app.hononeko.threadificatorr.core.StorageError
import app.hononeko.app.hononeko.threadificatorr.core.StorageError.ReadError
import app.hononeko.app.hononeko.threadificatorr.core.StorageError.SaveError
import app.hononeko.app.hononeko.threadificatorr.core.ThreadRepository
import app.hononeko.app.hononeko.threadificatorr.core.UnifiedNotification
import arrow.core.Either
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val INDEXES_PATH = "/indexes"
private const val SEARCH_PATH = "/search"
private const val DOCUMENTS_PATH = "/documents"
private const val SETTINGS_PATH = "/settings"
private const val HEALTH_PATH = "/health"

private const val MEDIA_ID_FIELD = "mediaId"
private const val MEDIA_NAME_FIELD = "mediaName"

class MeilisearchThreadRepository private constructor(
    val client: HttpClient, val indexName: String
) : ThreadRepository {

    companion object {
        suspend fun init(
            config: ApplicationConfig, client: HttpClient? = null
        ): ThreadRepository {
            val apiKey = config.property("meilisearch.apiKey").getString()
            val serviceUrl = config.property("meilisearch.host").getString()
            val indexName = config.property("meilisearch.indexName").getString()

            require(apiKey.isNotBlank()) { "API key must not be blank" }
            val httpClient = client?.config { 
                defaultRequest {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $apiKey")
                    }
                    contentType(ContentType.Application.Json)
                    url(serviceUrl)
                }
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.ALL
                }
            } ?: HttpClient(CIO).config {
                defaultRequest {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $apiKey")
                    }
                    contentType(ContentType.Application.Json)
                    url(serviceUrl)
                }
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.ALL
                }
            }

            try {
                httpClient.get(serviceUrl + HEALTH_PATH)
            } catch (e: Exception) {
                throw IllegalStateException("Meilisearch instance not available at $serviceUrl", e)
            }

            runBlocking { createIndex(httpClient, indexName) }
            configureIndexFilters(httpClient, indexName)
            return MeilisearchThreadRepository(httpClient, indexName)
        }
    }

    override suspend fun add(notification: UnifiedNotification): Either<StorageError, Unit> = Either.catch {
        client.post(URLBuilder(pathSegments = listOf(INDEXES_PATH, indexName, DOCUMENTS_PATH)).build()) {
            setBody(Json.encodeToString(notification))
        }
    }.mapLeft { SaveError }.map {  }

    override suspend fun get(mediaIdentifier: String): Either<StorageError, String> = Either.catch {
        val filter = "$MEDIA_NAME_FIELD = '$mediaIdentifier'"
        client.post(URLBuilder(pathSegments = listOf(INDEXES_PATH, indexName, SEARCH_PATH)).build()) {
            setBody(Json.encodeToString(SearchRequest(filter)))
        }.bodyAsText()
    }.mapLeft { ReadError }
}

private suspend inline fun <reified T> postAndForget(client: HttpClient, url: String, body: T) {
    client.post(url) {
        setBody(Json.encodeToString(body))
    }
}

private suspend fun createIndex(
    client: HttpClient, indexName: String
) = postAndForget(client, INDEXES_PATH, IndexCreationRequest(indexName, MEDIA_ID_FIELD))

private suspend fun configureIndexFilters(
    client: HttpClient,
    indexName: String,
) = postAndForget(
    client,
    URLBuilder(pathSegments = listOf(INDEXES_PATH, indexName, SETTINGS_PATH)).build().toString(),
    IndexSettings(
        searchableAttributes = listOf(MEDIA_ID_FIELD, MEDIA_NAME_FIELD),
        stopWords = listOf("of", "the"),
        filterableAttributes = listOf(MEDIA_ID_FIELD, MEDIA_NAME_FIELD),
        sortableAttributes = listOf(MEDIA_ID_FIELD)
    )
)
