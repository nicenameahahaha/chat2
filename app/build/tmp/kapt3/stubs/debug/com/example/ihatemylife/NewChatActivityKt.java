package com.example.ihatemylife;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000B\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0005\u001a\u001e\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001aF\u0010\u0006\u001a\u00020\u00012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u001e\u0010\b\u001a\u001a\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u000b\u0012\u0004\u0012\u00020\u00010\t2\u0006\u0010\f\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000eH\u0007\u001a\b\u0010\u000f\u001a\u00020\u0001H\u0007\u001aK\u0010\u0010\u001a\u00020\u00012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052.\u0010\u0011\u001a*\b\u0001\u0012\u0004\u0012\u00020\u0003\u0012\u0016\u0012\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\n0\u00130\u0012\u0012\u0006\u0012\u0004\u0018\u00010\u00150\tH\u0007\u00a2\u0006\u0002\u0010\u0016\u001a&\u0010\u0017\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0018\u001a\u00020\u00142\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u00a8\u0006\u001a"}, d2 = {"ContactItem", "", "contact", "Lcom/example/ihatemylife/Contact;", "onClick", "Lkotlin/Function0;", "GroupSelectionScreen", "onDismiss", "onCreateGroup", "Lkotlin/Function2;", "", "", "currentUserId", "contactRepository", "Lcom/example/ihatemylife/repository/ContactRepository;", "NewChatScreen", "NewContactBottomSheet", "onCreateContact", "Lkotlin/coroutines/Continuation;", "Lkotlin/Pair;", "", "", "(Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function2;)V", "SelectableContactItem", "isSelected", "onToggle", "app_debug"})
public final class NewChatActivityKt {
    
    @android.annotation.SuppressLint(value = {"RememberReturnType"})
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void NewChatScreen() {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void ContactItem(@org.jetbrains.annotations.NotNull()
    com.example.ihatemylife.Contact contact, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void NewContactBottomSheet(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super com.example.ihatemylife.Contact, ? super kotlin.coroutines.Continuation<? super kotlin.Pair<java.lang.Boolean, java.lang.String>>, ? extends java.lang.Object> onCreateContact) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void GroupSelectionScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.util.List<java.lang.String>, kotlin.Unit> onCreateGroup, @org.jetbrains.annotations.NotNull()
    java.lang.String currentUserId, @org.jetbrains.annotations.NotNull()
    com.example.ihatemylife.repository.ContactRepository contactRepository) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void SelectableContactItem(@org.jetbrains.annotations.NotNull()
    com.example.ihatemylife.Contact contact, boolean isSelected, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onToggle) {
    }
}