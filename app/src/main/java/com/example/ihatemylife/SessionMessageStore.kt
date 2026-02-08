package com.example.ihatemylife

import java.util.concurrent.ConcurrentHashMap

/**
 * In-memory message store scoped to the current app session, keyed by chat.
 * Each chat has its own isolated message list; nothing is shared between chats.
 * Used for local-only or optimistic messages within a chat session.
 */
object SessionMessageStore {
    private val store = ConcurrentHashMap<String, MutableList<Message>>()

    /**
     * Returns the session-local message list for this chat only.
     * Never returns messages from another chat.
     */
    fun getMessagesForChat(chatId: String): List<Message> =
        store[chatId]?.toList() ?: emptyList()

    /**
     * Adds a message to this chat's list only. Does not affect other chats.
     */
    fun addMessage(chatId: String, message: Message) {
        store.getOrPut(chatId) { mutableListOf() }.add(message)
    }

    /**
     * Removes a message from this chat's list (e.g. after it is persisted and received from Room).
     */
    fun removeMessage(chatId: String, messageId: Int) {
        store[chatId]?.removeAll { it.id == messageId }
    }

    /**
     * Clears session messages for this chat only. Other chats are unchanged.
     */
    fun clearChat(chatId: String) {
        store.remove(chatId)
    }

    /** Clears all session message data (e.g. on logout). */
    fun clearAll() {
        store.clear()
    }
}
