package app.hononeko.app.hononeko.threadificatorr.core

import arrow.core.Either

/**
 * Sends the processed notification to existing thread for media if exists
 * or to a new thread if it does not exist
 */
interface Notifier {
    fun notify(notification: UnifiedNotification): Either<Unit, NotifierError>
}

/**
 * Manages the store of existing threads mapped to media
 */
interface ThreadRepository {
    /**
     * Adds a new media ID - thread ID mapping to the known threads
     */
    suspend fun add(notification: UnifiedNotification): Either<StorageError, Unit>

    /**
     * Returns the message Thread ID if it exists
     */
    suspend fun get(mediaIdentifier: String): Either<StorageError, String>
}

/**
 * Parses webhook payloads from different services and normalizes the payloads into a unified format
 */
interface WebhookParser {
    fun parse(payload: String): Either<UnifiedNotification, ParserError>
}