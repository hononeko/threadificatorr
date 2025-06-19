package app.hononeko.app.hononeko.threadificatorr.core

/**
 * Sends the processed notification to existing thread for media if exists
 * or to a new thread if it does not exist
 */
interface Notifier {
    fun notify(notification: UnifiedNotification)
}

/**
 * Manages the store of existing threads mapped to media
 */
interface ThreadRepository {
    /**
     * Adds a new media ID - thread ID mapping to the known threads
     */
    fun add(notification: UnifiedNotification)

    /**
     * Returns the message Thread ID if it exists
     */
    fun get(mediaIdentifier: String): String?
}

/**
 * Parses webhook payloads from different services and normalizes the payloads into a unified format
 */
interface WebhookParser {
    fun parse(payload: String): UnifiedNotification
}