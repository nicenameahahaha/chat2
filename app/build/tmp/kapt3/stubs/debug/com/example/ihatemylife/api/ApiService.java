package com.example.ihatemylife.api;

/**
 * Retrofit interface for backend API endpoints (backend4).
 * Users, messages, integration webhook; link Telegram via PATCH /users/{username}/telegram.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u001e\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0007J.\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u00032\b\b\u0001\u0010\u000b\u001a\u00020\u00062\b\b\u0001\u0010\f\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\rJ\u001e\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\n0\u00032\b\b\u0001\u0010\u000f\u001a\u00020\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0011J$\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0007J$\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0007J(\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0016\u001a\u00020\u0017H\u00a7@\u00a2\u0006\u0002\u0010\u0018J\u001e\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\n0\u00032\b\b\u0001\u0010\u000f\u001a\u00020\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0011J(\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\n0\u00032\b\b\u0001\u0010\u000f\u001a\u00020\u00102\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u001bJ\u001e\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00150\u00032\b\b\u0001\u0010\u001d\u001a\u00020\u001eH\u00a7@\u00a2\u0006\u0002\u0010\u001fJ2\u0010 \u001a\b\u0012\u0004\u0012\u00020\n0\u00032\b\b\u0001\u0010!\u001a\u00020\u00062\b\b\u0001\u0010\"\u001a\u00020\u00062\b\b\u0001\u0010#\u001a\u00020$H\u00a7@\u00a2\u0006\u0002\u0010%\u00a8\u0006&"}, d2 = {"Lcom/example/ihatemylife/api/ApiService;", "", "getAllMessages", "Lretrofit2/Response;", "Lcom/example/ihatemylife/api/models/ApiAllMessagesResponse;", "username", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getConversation", "", "Lcom/example/ihatemylife/api/models/ApiMessageOut;", "username1", "username2", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMessage", "messageId", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getReceivedMessages", "getSentMessages", "linkUserTelegram", "Lcom/example/ihatemylife/api/models/ApiUserOut;", "body", "Lcom/example/ihatemylife/api/models/ApiUserTelegramLink;", "(Ljava/lang/String;Lcom/example/ihatemylife/api/models/ApiUserTelegramLink;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markMessageAsDelivered", "markMessageAsRead", "(ILjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "registerUser", "user", "Lcom/example/ihatemylife/api/models/ApiUserCreate;", "(Lcom/example/ihatemylife/api/models/ApiUserCreate;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendMessageToUser", "senderUsername", "receiverUsername", "message", "Lcom/example/ihatemylife/api/models/ApiMessageCreate;", "(Ljava/lang/String;Ljava/lang/String;Lcom/example/ihatemylife/api/models/ApiMessageCreate;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface ApiService {
    
    @retrofit2.http.POST(value = "users/")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object registerUser(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.example.ihatemylife.api.models.ApiUserCreate user, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.example.ihatemylife.api.models.ApiUserOut>> $completion);
    
    @retrofit2.http.PATCH(value = "users/{username}/telegram")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object linkUserTelegram(@retrofit2.http.Path(value = "username")
    @org.jetbrains.annotations.NotNull()
    java.lang.String username, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.example.ihatemylife.api.models.ApiUserTelegramLink body, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.example.ihatemylife.api.models.ApiUserOut>> $completion);
    
    @retrofit2.http.POST(value = "messages/send/{sender_username}/{receiver_username}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object sendMessageToUser(@retrofit2.http.Path(value = "sender_username")
    @org.jetbrains.annotations.NotNull()
    java.lang.String senderUsername, @retrofit2.http.Path(value = "receiver_username")
    @org.jetbrains.annotations.NotNull()
    java.lang.String receiverUsername, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.example.ihatemylife.api.models.ApiMessageCreate message, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.example.ihatemylife.api.models.ApiMessageOut>> $completion);
    
    @retrofit2.http.GET(value = "messages/sent/{username}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSentMessages(@retrofit2.http.Path(value = "username")
    @org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.example.ihatemylife.api.models.ApiMessageOut>>> $completion);
    
    @retrofit2.http.GET(value = "messages/received/{username}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getReceivedMessages(@retrofit2.http.Path(value = "username")
    @org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.example.ihatemylife.api.models.ApiMessageOut>>> $completion);
    
    @retrofit2.http.GET(value = "messages/all/{username}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllMessages(@retrofit2.http.Path(value = "username")
    @org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.example.ihatemylife.api.models.ApiAllMessagesResponse>> $completion);
    
    @retrofit2.http.GET(value = "messages/conversation/{username1}/{username2}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getConversation(@retrofit2.http.Path(value = "username1")
    @org.jetbrains.annotations.NotNull()
    java.lang.String username1, @retrofit2.http.Path(value = "username2")
    @org.jetbrains.annotations.NotNull()
    java.lang.String username2, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.example.ihatemylife.api.models.ApiMessageOut>>> $completion);
    
    @retrofit2.http.GET(value = "messages/{message_id}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMessage(@retrofit2.http.Path(value = "message_id")
    int messageId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.example.ihatemylife.api.models.ApiMessageOut>> $completion);
    
    @retrofit2.http.PATCH(value = "messages/{message_id}/read/{username}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object markMessageAsRead(@retrofit2.http.Path(value = "message_id")
    int messageId, @retrofit2.http.Path(value = "username")
    @org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.example.ihatemylife.api.models.ApiMessageOut>> $completion);
    
    @retrofit2.http.PATCH(value = "messages/{message_id}/delivered")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object markMessageAsDelivered(@retrofit2.http.Path(value = "message_id")
    int messageId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.example.ihatemylife.api.models.ApiMessageOut>> $completion);
}