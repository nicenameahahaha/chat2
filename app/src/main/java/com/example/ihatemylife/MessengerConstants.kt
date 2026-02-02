package com.example.ihatemylife

/**
 * Constants used for "smart client" behavior without backend changes.
 * Typing indicators are implemented as system messages with this content;
 * they are filtered out from the displayed message list and used only for "X is typing" UI.
 */
object MessengerConstants {
    /** Content of a message used as a typing indicator. Filter these from the chat list. */
    const val TYPING_INDICATOR_CONTENT = "[TYPING]"

    /** Consider "typing" active if we received a typing message within this many ms. */
    const val TYPING_INDICATOR_ACTIVE_MS = 8_000L

    /** Debounce interval (ms) when sending typing indicators while user is typing. */
    const val TYPING_SEND_DEBOUNCE_MS = 2_000L

    /** Consider user "active now" (presence) if last activity is within this many ms. */
    const val PRESENCE_ACTIVE_NOW_MS = 120_000L
}
