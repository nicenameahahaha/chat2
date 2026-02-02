package com.example.ihatemylife.viewmodel;

/**
 * ViewModel for NewChatActivity
 * Manages contact list, search, and sorting
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J(\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\n2\u0018\u0010!\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0#\u0012\u0004\u0012\u00020\u001f0\"J\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\n0\tJ\b\u0010%\u001a\u00020\u001fH\u0002J\u000e\u0010&\u001a\u00020\u001f2\u0006\u0010\'\u001a\u00020\u0005J\u0006\u0010(\u001a\u00020\u001fR\u001a\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00050\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\f0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0017\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\f0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0014R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00050\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0014R\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\f0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0014R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006)"}, d2 = {"Lcom/example/ihatemylife/viewmodel/NewChatViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "currentUserId", "", "(Landroid/app/Application;Ljava/lang/String;)V", "_contacts", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/example/ihatemylife/Contact;", "_isLoading", "", "_searchQuery", "_sortAscending", "contactRepository", "Lcom/example/ihatemylife/repository/ContactRepository;", "contacts", "Lkotlinx/coroutines/flow/StateFlow;", "getContacts", "()Lkotlinx/coroutines/flow/StateFlow;", "getCurrentUserId", "()Ljava/lang/String;", "isLoading", "searchQuery", "getSearchQuery", "sortAscending", "getSortAscending", "userRepository", "Lcom/example/ihatemylife/repository/UserRepository;", "addContact", "", "contact", "onResult", "Lkotlin/Function1;", "Lkotlin/Result;", "getFilteredContacts", "loadContacts", "setSearchQuery", "query", "toggleSort", "app_debug"})
public final class NewChatViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String currentUserId = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.ihatemylife.repository.ContactRepository contactRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.ihatemylife.repository.UserRepository userRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.example.ihatemylife.Contact>> _contacts = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.example.ihatemylife.Contact>> contacts = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _searchQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> searchQuery = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _sortAscending = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> sortAscending = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isLoading = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isLoading = null;
    
    public NewChatViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application, @org.jetbrains.annotations.NotNull()
    java.lang.String currentUserId) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCurrentUserId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.example.ihatemylife.Contact>> getContacts() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getSearchQuery() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getSortAscending() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isLoading() {
        return null;
    }
    
    /**
     * Load contacts (user-created + system users)
     */
    private final void loadContacts() {
    }
    
    /**
     * Get filtered and sorted contacts
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.example.ihatemylife.Contact> getFilteredContacts() {
        return null;
    }
    
    /**
     * Set search query
     */
    public final void setSearchQuery(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    /**
     * Toggle sort order
     */
    public final void toggleSort() {
    }
    
    /**
     * Add contact
     */
    public final void addContact(@org.jetbrains.annotations.NotNull()
    com.example.ihatemylife.Contact contact, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super kotlin.Result<com.example.ihatemylife.Contact>, kotlin.Unit> onResult) {
    }
}