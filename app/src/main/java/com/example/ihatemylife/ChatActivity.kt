package com.example.ihatemylife

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.offset
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.CardDefaults
import com.example.ihatemylife.ui.theme.IhatemylifeTheme
import com.example.ihatemylife.viewmodel.ChatViewModel
import kotlin.math.absoluteValue
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ChatActivity : ComponentActivity() {
    private var lastAppliedDarkTheme: Boolean? = null

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val chatId = intent.getStringExtra("chat_id") ?: ""
        val chatTitle = intent.getStringExtra("chat_title") ?: "Chat"
        val userPrefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val currentUsername = userPrefs.getString("username", "") ?: ""
        val otherUsername = intent.getStringExtra("other_username")?.takeIf { it.isNotBlank() }
        applyThemeAndContent(chatId, chatTitle, currentUsername, otherUsername)
    }

    override fun onResume() {
        super.onResume()
        val themePrefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val currentTheme = themePrefs.getBoolean("dark_theme", true)
        if (lastAppliedDarkTheme != null && lastAppliedDarkTheme != currentTheme) {
            recreate()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    private fun applyThemeAndContent(
        chatId: String,
        chatTitle: String,
        currentUsername: String,
        otherUsername: String?
    ) {
        val themePrefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val isDarkTheme = themePrefs.getBoolean("dark_theme", true)
        lastAppliedDarkTheme = isDarkTheme
        setContent {
            IhatemylifeTheme(darkTheme = isDarkTheme) {
                ChatScreen(
                    chatId = chatId,
                    chatTitle = chatTitle,
                    currentUsername = currentUsername,
                    otherUsername = otherUsername
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatId: String,
    chatTitle: String,
    currentUsername: String,
    otherUsername: String?
) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val application = context.applicationContext as? android.app.Application
    
    // Create ViewModel
    val viewModel: ChatViewModel = if (application != null) {
        remember {
            ChatViewModel(application, chatId, currentUsername, otherUsername)
        }
    } else {
        // Fallback - should not happen
        return
    }
    
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val replyToMessage by viewModel.replyToMessage.collectAsState()
    val otherUserTyping by viewModel.otherUserTyping.collectAsState()
    val otherUserLastSeen by viewModel.otherUserLastSeen.collectAsState()
    
    var messageText by remember { mutableStateOf("") }
    val hasMessage = messageText.isNotBlank()

    // Debounced typing indicator: notify other user when we're typing; stop when we send or clear
    LaunchedEffect(messageText) {
        if (messageText.isNotBlank()) {
            kotlinx.coroutines.delay(400L)
            if (messageText.isNotBlank()) viewModel.setUserTyping(true)
        } else {
            viewModel.stopTyping()
        }
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.stopTyping() }
    }

    val filterPrefs = context.getSharedPreferences("chat_filters_$chatId", Context.MODE_PRIVATE)
    var messengerFilter by remember { mutableStateOf(filterPrefs.getString("messenger", "all") ?: "all") }
    var messageSizeFilter by remember { mutableStateOf(filterPrefs.getString("message_size", "all") ?: "all") }
    var searchQuery by remember { mutableStateOf("") }
    var showSearchBar by remember { mutableStateOf(false) }
    var showFiltersSheet by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }

    // Mute state (per-chat notifications)
    val mutePrefs = context.getSharedPreferences("chat_mutes", Context.MODE_PRIVATE)
    var isMuted by remember {
        mutableStateOf(mutePrefs.getBoolean("chat_muted_$chatId", false))
    }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    
    // Apply per-chat filters and search, then group.
    // Source filter: "telegram" = only messages from Telegram (cross-checked via backend/DB source field);
    // "own_messenger" = only messages sent within the messenger (no external sources).
    val filteredMessages = remember(messages, searchQuery, messageSizeFilter, messengerFilter) {
        var list = messages
        when (messengerFilter) {
            "telegram" -> list = list.filter { it.source == "telegram" }
            "own_messenger" -> list = list.filter { it.source == "own_messenger" }
            else -> { /* "all" */ }
        }
        if (searchQuery.isNotBlank()) {
            list = list.filter { it.content.contains(searchQuery, ignoreCase = true) }
        }
        when (messageSizeFilter) {
            "short" -> list = list.filter { it.content.length < 50 }
            "medium" -> list = list.filter { it.content.length in 50..200 }
            "long" -> list = list.filter { it.content.length > 200 }
        }
        list
    }
    val groupedMessages = remember(filteredMessages) {
        groupMessages(filteredMessages, viewModel.currentUserId)
    }
    
    // Auto-scroll to bottom when new messages arrive (only if near bottom)
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            val isNearBottom = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index 
                ?: 0 >= messages.size - 3
            if (isNearBottom) {
                listState.animateScrollToItem(groupedMessages.size - 1)
            }
        }
    }
    
    // Refresh state
    val isRefreshing = isLoading
    
    // Subtitle: typing indicator or last seen
    val presenceSubtitle = remember(otherUserTyping, otherUserLastSeen) {
        when {
            otherUserTyping -> "typing..."
            otherUserLastSeen != null -> {
                val ts = otherUserLastSeen!!
                val now = System.currentTimeMillis()
                if ((now - ts) < com.example.ihatemylife.MessengerConstants.PRESENCE_ACTIVE_NOW_MS)
                    "active now"
                else
                    "last seen ${formatLastSeen(ts)}"
            }
            else -> null
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(chatTitle)
                        if (presenceSubtitle != null) {
                            Text(
                                text = presenceSubtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            activity?.finish()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    // Mute button (slightly left: reduced padding)
                    if (isMuted) {
                        Text(
                            text = "muted",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clickable {
                                    isMuted = false
                                    mutePrefs.edit()
                                        .putBoolean("chat_muted_$chatId", false)
                                        .apply()
                                }
                        )
                    } else {
                        IconButton(
                            onClick = {
                                isMuted = true
                                mutePrefs.edit()
                                    .putBoolean("chat_muted_$chatId", true)
                                    .apply()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Mute"
                            )
                        }
                    }
                    // Overflow menu (⋮)
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu"
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Filters") },
                                onClick = {
                                    menuExpanded = false
                                    showFiltersSheet = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Search") },
                                onClick = {
                                    menuExpanded = false
                                    showSearchBar = !showSearchBar
                                }
                            )
                        }
                    }
                }
            )
        }
    , containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Column(modifier = Modifier.fillMaxSize()) {
            // Reply preview (if replying to a message)
            if (replyToMessage != null) {
                ReplyPreview(
                    message = replyToMessage!!,
                    onDismiss = { viewModel.setReplyToMessage(null) }
                )
            }

            // Search bar (search messages by entered characters)
            if (showSearchBar) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search messages...") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.outline,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null
                            )
                        }
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    IconButton(
                        onClick = { showSearchBar = false; searchQuery = "" }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close search"
                        )
                    }
                }
            }

            // Message list
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = listState,
                reverseLayout = false,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(groupedMessages.size) { index ->
                    val item = groupedMessages[index]
                    when (item) {
                        is MessageGroupItem.DateSeparator -> {
                            DateSeparator(item.date)
                        }
                        is MessageGroupItem.MessageItem -> {
                            val prevItem = if (index > 0) groupedMessages[index - 1] else null
                            val showAvatar = prevItem !is MessageGroupItem.MessageItem || 
                                prevItem.message.senderId != item.message.senderId ||
                                (prevItem.message.timestamp - item.message.timestamp).absoluteValue > 300000 // 5 minutes
                            
                            val repliedMsg = if (item.message.replyToMessageId != null) {
                                messages.find { it.id == item.message.replyToMessageId }
                            } else null
                            
                            MessageBubble(
                                message = item.message,
                                isSentByMe = item.message.senderId == viewModel.currentUserId || viewModel.isCreatedContactChat,
                                showAvatar = showAvatar,
                                repliedMessage = repliedMsg,
                                onSwipeToReply = {
                                    viewModel.setReplyToMessage(item.message)
                                }
                            )
                        }
                    }
                }
            }
            
            // Bottom input panel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Type a message...") },
                    modifier = Modifier.weight(1f),
                    singleLine = false,
                    maxLines = 20,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
                
                Button(
                    onClick = {
                        if (hasMessage) {
                            viewModel.stopTyping()
                            viewModel.sendMessage(messageText)
                            messageText = ""
                        }
                    },
                    enabled = hasMessage && !isLoading
                ) {
                    Text("Send")
                }
            }
            }

            // Per-chat filters bottom sheet
                if (showFiltersSheet) {
                val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                var messengerExpanded by remember { mutableStateOf(false) }
                var messageSizeExpanded by remember { mutableStateOf(false) }
                ModalBottomSheet(
                    onDismissRequest = { showFiltersSheet = false },
                    sheetState = sheetState
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Messenger type filter",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Box {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { messengerExpanded = true },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Text(
                                    text = messengerFilter.replaceFirstChar { it.uppercase() }.replace("_", " "),
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            DropdownMenu(
                                expanded = messengerExpanded,
                                onDismissRequest = { messengerExpanded = false }
                            ) {
                                listOf("all", "own_messenger", "telegram").forEach { opt ->
                                    DropdownMenuItem(
                                        text = { Text(opt.replaceFirstChar { it.uppercase() }.replace("_", " ")) },
                                        onClick = {
                                            messengerFilter = opt
                                            filterPrefs.edit().putString("messenger", opt).apply()
                                            messengerExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Message size filter",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Box {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { messageSizeExpanded = true },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Text(
                                    text = when (messageSizeFilter) {
                                        "short" -> "Short (<50 chars)"
                                        "medium" -> "Medium (50-200 chars)"
                                        "long" -> "Long (>200 chars)"
                                        else -> "All"
                                    },
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            DropdownMenu(
                                expanded = messageSizeExpanded,
                                onDismissRequest = { messageSizeExpanded = false }
                            ) {
                                listOf("all", "short", "medium", "long").forEach { opt ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                when (opt) {
                                                    "short" -> "Short (<50 chars)"
                                                    "medium" -> "Medium (50-200 chars)"
                                                    "long" -> "Long (>200 chars)"
                                                    else -> "All"
                                                }
                                            )
                                        },
                                        onClick = {
                                            messageSizeFilter = opt
                                            filterPrefs.edit().putString("message_size", opt).apply()
                                            messageSizeExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(
    message: Message,
    isSentByMe: Boolean,
    showAvatar: Boolean = false,
    onSwipeToReply: () -> Unit,
    repliedMessage: Message? = null
) {
    var showAbsoluteTime by remember { mutableStateOf(false) }
    var swipeOffset by remember { mutableStateOf(0f) }
    
    // Sent vs received: distinct alignment and styling
    val backgroundColor = if (isSentByMe) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val textColor = if (isSentByMe) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    val bubbleShape = RoundedCornerShape(
        topStart = 18.dp,
        topEnd = 18.dp,
        bottomStart = if (isSentByMe) 18.dp else 6.dp,
        bottomEnd = if (isSentByMe) 6.dp else 18.dp
    )
    val borderColor = if (isSentByMe) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }

    // Animate swipe offset
    val animatedOffset by animateFloatAsState(
        targetValue = swipeOffset.coerceIn(0f, 50f),
        animationSpec = tween(200),
        label = "swipe_offset"
    )
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 2.dp),
        horizontalArrangement = if (isSentByMe) Arrangement.End else Arrangement.Start
    ) {
        if (!isSentByMe && showAvatar) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "U",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.widthIn(8.dp))
        } else if (!isSentByMe) {
            Spacer(modifier = Modifier.widthIn(40.dp))
        }
        
        Card(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .offset(x = animatedOffset.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (swipeOffset > 30f) {
                                onSwipeToReply()
                            }
                            swipeOffset = 0f
                        }
                    ) { change, dragAmount ->
                        if (!isSentByMe && dragAmount > 0) {
                            swipeOffset = dragAmount.coerceIn(0f, 50f)
                        } else if (isSentByMe && dragAmount < 0) {
                            swipeOffset = (-dragAmount).coerceIn(0f, 50f)
                        }
                    }
                },
            shape = bubbleShape,
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            border = BorderStroke(1.dp, borderColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                // Reply context (if replying to a message)
                if (message.replyToMessageId != null && repliedMessage != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                textColor.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Column {
                            Text(
                                text = "↩ ${repliedMessage.content.take(50)}${if (repliedMessage.content.length > 50) "..." else ""}",
                                style = MaterialTheme.typography.bodySmall,
                                color = textColor.copy(alpha = 0.8f),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                } else if (message.replyToMessageId != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                textColor.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "↩ Replying to message",
                            style = MaterialTheme.typography.bodySmall,
                            color = textColor.copy(alpha = 0.8f)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
                
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (showAbsoluteTime) {
                            formatAbsoluteTimestamp(message.timestamp)
                        } else {
                            formatTimestamp(message.timestamp)
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = textColor.copy(alpha = 0.7f),
                        modifier = Modifier
                            .padding(top = 4.dp, start = 8.dp)
                            .pointerInput(Unit) {
                                // Long press to show absolute time
                            }
                    )
                    
                    // Message status indicators (for sent messages)
                    if (isSentByMe) {
                        Spacer(modifier = Modifier.widthIn(4.dp))
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = if (message.isRead) "Read" else if (message.isDelivered) "Delivered" else "Sent",
                            modifier = Modifier.size(14.dp),
                            tint = if (message.isRead) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                textColor.copy(alpha = 0.7f)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DateSeparator(date: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = date,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

// Helper function to group messages
private fun groupMessages(
    messages: List<Message>,
    currentUserId: Int?
): List<MessageGroupItem> {
    if (messages.isEmpty()) return emptyList()
    
    val grouped = mutableListOf<MessageGroupItem>()
    val calendar = Calendar.getInstance()
    var lastDate: String? = null
    
    messages.forEachIndexed { index, message ->
        calendar.timeInMillis = message.timestamp
        val messageDate = formatDate(calendar)
        
        // Add date separator if date changed
        if (messageDate != lastDate) {
            grouped.add(MessageGroupItem.DateSeparator(messageDate))
            lastDate = messageDate
        }
        
        grouped.add(MessageGroupItem.MessageItem(message))
    }
    
    return grouped
}

private fun formatDate(calendar: Calendar): String {
    val today = Calendar.getInstance()
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    
    return when {
        isSameDay(calendar, today) -> "Today"
        isSameDay(calendar, yesterday) -> "Yesterday"
        else -> SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(calendar.time)
    }
}

private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

sealed class MessageGroupItem {
    data class DateSeparator(val date: String) : MessageGroupItem()
    data class MessageItem(val message: Message) : MessageGroupItem()
}

@Composable
fun ReplyPreview(
    message: Message,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Replying to",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
                Text(
                    text = message.content.take(50) + if (message.content.length > 50) "..." else "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onDismiss) {
                Text("✕", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

private fun formatLastSeen(timestamp: Long): String {
    val date = Date(timestamp)
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
    val today = Calendar.getInstance()
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    return when {
        diff < 60_000 -> "just now"
        diff < 3600_000 -> "${diff / 60_000} min ago"
        diff < 86400_000 && isSameDay(cal, today) -> "today at ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)}"
        diff < 172800_000 && isSameDay(cal, yesterday) -> "yesterday at ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)}"
        else -> SimpleDateFormat("MMM d at HH:mm", Locale.getDefault()).format(date)
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60000 -> "now"
        diff < 3600000 -> "${diff / 60000}m"
        diff < 86400000 -> "${diff / 3600000}h"
        else -> SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
    }
}

private fun formatAbsoluteTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    return SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault()).format(date)
}

