package com.example.ihatemylife;

/**
 * Constants used for "smart client" behavior without backend changes.
 * Typing indicators are implemented as system messages with this content;
 * they are filtered out from the displayed message list and used only for "X is typing" UI.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/example/ihatemylife/MessengerConstants;", "", "()V", "PRESENCE_ACTIVE_NOW_MS", "", "TYPING_INDICATOR_ACTIVE_MS", "TYPING_INDICATOR_CONTENT", "", "TYPING_SEND_DEBOUNCE_MS", "app_debug"})
public final class MessengerConstants {
    
    /**
     * Content of a message used as a typing indicator. Filter these from the chat list.
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TYPING_INDICATOR_CONTENT = "[TYPING]";
    
    /**
     * Consider "typing" active if we received a typing message within this many ms.
     */
    public static final long TYPING_INDICATOR_ACTIVE_MS = 8000L;
    
    /**
     * Debounce interval (ms) when sending typing indicators while user is typing.
     */
    public static final long TYPING_SEND_DEBOUNCE_MS = 2000L;
    
    /**
     * Consider user "active now" (presence) if last activity is within this many ms.
     */
    public static final long PRESENCE_ACTIVE_NOW_MS = 120000L;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.ihatemylife.MessengerConstants INSTANCE = null;
    
    private MessengerConstants() {
        super();
    }
}