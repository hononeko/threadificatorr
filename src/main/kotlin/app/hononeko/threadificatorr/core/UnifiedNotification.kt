package app.hononeko.app.hononeko.threadificatorr.core

import kotlinx.serialization.Serializable

@Serializable
data class UnifiedNotification(
    val mediaId: String,
    val mediaName: String,
    val notificationText: String,
)
