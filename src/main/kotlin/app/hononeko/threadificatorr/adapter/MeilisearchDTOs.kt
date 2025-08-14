package app.hononeko.app.hononeko.threadificatorr.adapter

import kotlinx.serialization.Serializable

@Serializable
data class SearchRequest(val filter: String)

@Serializable
data class IndexCreationRequest(val uid: String, val primaryKey: String)

@Serializable
data class IndexSettings(
    val searchableAttributes: List<String>,
    val stopWords: List<String>,
    val filterableAttributes: List<String>,
    val sortableAttributes: List<String>
)
