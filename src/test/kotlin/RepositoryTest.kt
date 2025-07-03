package app.hononeko

import app.hononeko.app.hononeko.threadificatorr.adapter.MeilisearchThreadRepository

class RepositoryTest {
    private val repository = MeilisearchThreadRepository(
        apiKey = "391a5ff7-deba-4dc1-a4ee-e6885701b65b",
        serviceUrl = "http://localhost:7700/"
    )

}