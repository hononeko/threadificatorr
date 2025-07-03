package app.hononeko.app.hononeko.threadificatorr.adapter

import app.hononeko.app.hononeko.threadificatorr.core.ThreadRepository
import app.hononeko.app.hononeko.threadificatorr.core.UnifiedNotification
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.util.*

class MeilisearchThreadRepository(
    val apiKey: String, val serviceUrl: String
) : ThreadRepository {
    private val client: HttpClient
    private val indexName = "threads"

    init {
        require(apiKey.isNotBlank()) { "API key must not be blank" }
        client = HttpClient(CIO).config {
            headers {
                append(HttpHeaders.Authorization, "Bearer $apiKey")
            }
            url {
                host = serviceUrl
                protocol = URLProtocol.HTTP
            }
        }
        suspend {
            createIndex()
        }
    }

    override suspend fun add(notification: UnifiedNotification) {
        client.post("/indexes/$indexName/documents/") {
            contentType(ContentType.Application.Json)
            setBody(notification)
        }
    }

    override suspend fun get(mediaIdentifier: String): String? {
        TODO("Not yet implemented")
    }

    private suspend fun createIndex(): HttpResponse = client.post("/indexes") {
        contentType(ContentType.Application.Json)
        setBody(
            """
           {
             "uid": "$indexName",
             "primaryKey": "id"
           }
           """.trimIndent()
        )
    }
}