package com.example.ihatemylife

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ihatemylife.ui.theme.IhatemylifeTheme
import com.example.ihatemylife.viewmodel.ChatsViewModel
import kotlinx.coroutines.launch
import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.BorderStroke
import androidx.lifecycle.lifecycleScope
import com.example.ihatemylife.repository.MessageRepository
import kotlinx.coroutines.launch

class ChatsActivity : ComponentActivity() {
    private var lastAppliedDarkTheme: Boolean? = null

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        applyThemeAndContent()
    }

    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val currentTheme = prefs.getBoolean("dark_theme", true)
        if (lastAppliedDarkTheme != null && lastAppliedDarkTheme != currentTheme) {
            recreate()
        }
        // Sync messages so chat list and unread/last message stay fresh when returning to the app
        val userPrefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val username = userPrefs.getString("username", "") ?: ""
        if (username.isNotBlank()) {
            lifecycleScope.launch {
                MessageRepository(applicationContext).syncMessages(username)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    private fun applyThemeAndContent() {
        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val isDarkTheme = prefs.getBoolean("dark_theme", true)
        lastAppliedDarkTheme = isDarkTheme
        setContent {
            IhatemylifeTheme(darkTheme = isDarkTheme) {
                ChatsScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as? Application
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val email = prefs.getString("user_identifier", "email@example.com") ?: "email@example.com"
    val user = DatabaseHelper.userByEmail(email)
    val username = user?.username ?: ""

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Use ViewModel for chat list
    val viewModel: ChatsViewModel? = if (application != null) {
        viewModel { ChatsViewModel(application) }
    } else {
        null
    }
    
    val activeChats by viewModel?.chats?.collectAsState() ?: remember { mutableStateOf(emptyList<Chat>()) }

    // Handle back button: open drawer if closed, quit app if drawer is open
    BackHandler(enabled = true) {
        scope.launch {
            if (drawerState.isClosed) {
                drawerState.open()
            } else {
                drawerState.close()
                // Move app to background (like pressing home button)
                (context as? Activity)?.moveTaskToBack(true)
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.75f)
            ) {
                val avatarPath = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    .getString("avatar_path", null)
                DrawerHeader(username = username, email = email, avatarPath = avatarPath)

                NavigationDrawerItem(
                    label = { Text("My Profile") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        context.startActivity(Intent(context, MyProfileActivity::class.java))
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Settings") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        context.startActivity(Intent(context, SettingsActivity::class.java))
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Log out") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        (context as? Activity)?.let { SessionHelper.logoutAndNavigateToRegister(it) }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Chats") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (drawerState.isClosed) {
                                        drawerState.open()
                                    } else {
                                        drawerState.close()
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        context.startActivity(Intent(context, NewChatActivity::class.java))
                    },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Text(
                        text = "+",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            if (activeChats.isEmpty()) {
                // No active chats: preserve existing behavior and UI.
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Chats",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            } else {
                // Active chats exist: display them.
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(activeChats) { chat ->
                        val mutePrefs = context.getSharedPreferences("chat_mutes", Context.MODE_PRIVATE)
                        val isMuted = mutePrefs.getBoolean("chat_muted_${chat.id}", false)
                        
                        ChatItem(
                            chat = chat,
                            isMuted = isMuted,
                            onClick = {
                                // Navigate to ChatActivity
                                val intent = Intent(context, ChatActivity::class.java).apply {
                                    putExtra("chat_id", chat.id)
                                    putExtra("chat_title", chat.title)
                                }
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatItem(
    chat: Chat,
    isMuted: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = chat.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (isMuted) {
                        Text(
                            text = "muted",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = chat.lastMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isMuted) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}

@Composable
fun DrawerHeader(username: String, email: String, avatarPath: String? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val bitmap = remember(avatarPath) {
                avatarPath?.let { BitmapFactory.decodeFile(it)?.asImageBitmap() }
            }
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        if (bitmap != null) Color.Transparent else MaterialTheme.colorScheme.surfaceVariant
                    )
            ) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                if (username.isNotEmpty()) {
                    Text(
                        text = username,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

