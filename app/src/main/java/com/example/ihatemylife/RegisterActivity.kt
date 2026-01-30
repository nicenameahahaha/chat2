package com.example.ihatemylife

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ihatemylife.ui.theme.IhatemylifeTheme
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.launch

data class User(
    val email: String,
    val username: String,
    val password: String
)

/**
 * Simple in-memory chat model.
 *
 * For now, a chat is considered "active" if its isActive flag is true.
 * This can be easily extended later (e.g., based on lastUpdated timestamps or status).
 */
data class Chat(
    val id: String,
    val title: String,
    val lastMessage: String,
    val isActive: Boolean = true,
    val lastUpdated: Long = System.currentTimeMillis(),
    val isGroup: Boolean = false,
    val participantIds: List<String> = emptyList(), // For group chats
    val isMuted: Boolean = false
)

/**
 * Message model for chat messages
 * Maps to backend Message model (sender_id, receiver_id)
 * Includes reply support for future implementation
 */
data class Message(
    val id: Int,
    val senderId: Int,
    val receiverId: Int? = null,
    val content: String,
    val timestamp: Long, // Epoch milliseconds
    val isDelivered: Boolean = false,
    val isRead: Boolean = false,
    val readAt: Long? = null,
    val source: String = "own_messenger", // "own_messenger" or "telegram"
    val replyToMessageId: Int? = null // For reply system
)

/**
 * Contact model for storing contact information.
 * Contacts can be registered users or external contacts.
 */
data class Contact(
    val id: String,
    val firstName: String,
    val lastName: String = "",
    val email: String? = null,
    val phone: String? = null,
    val username: String? = null // If contact is a registered user
)

/**
 * Very simple in-memory "database".
 * In a real app, replace this with a proper DatabaseHelper / repository.
 */
object DatabaseHelper {
    private val users = mutableListOf<User>()
    // Backing list for chats; using Compose state so UI can react to changes.
    private val chats = mutableStateListOf<Chat>()
    // User-scoped contacts: userId -> List<Contact>
    // System users are stored in 'users' list, user-created contacts are stored here per user
    private val userContacts = mutableMapOf<String, MutableList<Contact>>()

    fun userByEmail(email: String): User? =
        users.find { it.email.equals(email, ignoreCase = true) }

    fun userByUsername(username: String): User? =
        users.find { it.username.equals(username, ignoreCase = true) }

    fun addUser(user: User) {
        users.add(user)
    }

    /**
     * Get all registered users (excluding current user if needed).
     */
    fun getAllUsers(): List<User> = users.toList()

    /**
     * Search users by email, phone, or username.
     */
    fun searchUsers(query: String): List<User> {
        val lowerQuery = query.lowercase()
        return users.filter {
            it.email.lowercase().contains(lowerQuery) ||
            it.username.lowercase().contains(lowerQuery)
        }
    }

    /**
     * Add a contact for a specific user (user-created contact).
     * System users are stored separately in 'users' list.
     * 
     * @param userId The ID of the user adding the contact
     * @param contact The contact to add
     * @return Pair<Boolean, String> where Boolean indicates success, String is error message if duplicate
     */
    fun addContact(userId: String, contact: Contact): Pair<Boolean, String> {
        val contacts = userContacts.getOrPut(userId) { mutableListOf() }
        
        // Duplicate contact check: check by phone number OR email (per-user scoped)
        val duplicateByEmail = contact.email != null && contacts.any { 
            it.email != null && it.email.equals(contact.email, ignoreCase = true) 
        }
        val duplicateByPhone = contact.phone != null && contacts.any { 
            it.phone != null && it.phone == contact.phone 
        }
        
        if (duplicateByEmail || duplicateByPhone) {
            val duplicateField = if (duplicateByEmail) "email" else "phone number"
            return Pair(false, "Contact with this $duplicateField already exists")
        }
        
        contacts.add(contact)
        return Pair(true, "")
    }
    
    /**
     * Remove all user-created contacts for a specific user.
     * System users are not affected.
     * 
     * @param userId The ID of the user whose contacts should be cleared
     */
    fun clearUserContacts(userId: String) {
        userContacts.remove(userId)
    }
    
    /**
     * Remove all user-created contacts for all users.
     * System users are not affected.
     * Use with caution - this clears all user-created contacts across all users.
     */
    fun clearAllUserContacts() {
        userContacts.clear()
    }

    /**
     * Get all user-created contacts for a specific user.
     */
    fun getUserContacts(userId: String): List<Contact> {
        return userContacts[userId]?.toList() ?: emptyList()
    }

    /**
     * Get all available users/contacts for display for a specific user.
     * Combines system users (registered users) and user-created contacts.
     * System users are visible to all, user-created contacts are user-scoped.
     */
    fun getAllAvailableUsers(userId: String): List<Contact> {
        // System users (registered users) - visible to all
        val systemUserContacts = users.map { user ->
            Contact(
                id = user.email,
                firstName = user.username,
                lastName = "",
                email = user.email,
                username = user.username
            )
        }
        // User-created contacts - only for this user
        val userCreatedContacts = getUserContacts(userId)
        return (systemUserContacts + userCreatedContacts).distinctBy { it.id }
    }

    /**
     * Search contacts/users by first name, last name, email, phone, or username.
     * Searches within system users and user's own contacts.
     */
    fun searchContacts(userId: String, query: String): List<Contact> {
        val lowerQuery = query.lowercase()
        return getAllAvailableUsers(userId).filter {
            it.firstName.lowercase().contains(lowerQuery) ||
            it.lastName.lowercase().contains(lowerQuery) ||
            (it.email != null && it.email.lowercase().contains(lowerQuery)) ||
            (it.phone != null && it.phone.contains(lowerQuery)) ||
            (it.username != null && it.username.lowercase().contains(lowerQuery))
        }
    }

    /**
     * Create a group chat with selected participant IDs.
     */
    fun createGroupChat(title: String, participantIds: List<String>): Chat {
        val groupChat = Chat(
            id = "group_${System.currentTimeMillis()}",
            title = title,
            lastMessage = "Group created",
            isActive = true,
            isGroup = true,
            participantIds = participantIds
        )
        chats.add(groupChat)
        return groupChat
    }

    /**
     * Add or update chats.
     * These helpers avoid duplication and keep chat mutation logic centralized.
     */
    fun addChat(chat: Chat) {
        chats.add(chat)
    }

    fun updateChat(chat: Chat) {
        val index = chats.indexOfFirst { it.id == chat.id }
        if (index >= 0) {
            chats[index] = chat
        } else {
            chats.add(chat)
        }
    }

    fun setChatActive(chatId: String, active: Boolean) {
        val index = chats.indexOfFirst { it.id == chatId }
        if (index >= 0) {
            val current = chats[index]
            chats[index] = current.copy(
                isActive = active,
                lastUpdated = System.currentTimeMillis()
            )
        }
    }

    /**
     * Centralized filter for active chats.
     * UI code should use this instead of duplicating filtering logic.
     */
    fun getActiveChats(): List<Chat> =
        chats.filter { it.isActive }
}

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val isDarkTheme = prefs.getBoolean("dark_theme", true)
        setContent {
            IhatemylifeTheme(darkTheme = isDarkTheme) {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    RegisterScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun RegisterScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }
    var tosError by remember { mutableStateOf("") }

    var agreeTos by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true
        )
        if (emailError.isNotEmpty()) {
            Text(
                text = emailError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true
        )
        if (usernameError.isNotEmpty()) {
            Text(
                text = usernameError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        if (passwordError.isNotEmpty()) {
            Text(
                text = passwordError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        if (confirmPasswordError.isNotEmpty()) {
            Text(
                text = confirmPasswordError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                // reset errors
                emailError = ""
                usernameError = ""
                passwordError = ""
                confirmPasswordError = ""
                tosError = ""

                // 1) email checks
                if (email.contains(" ")) {
                    emailError = "Email must not contain spaces"
                } else if (!email.contains("@")) {
                    emailError = "Email must contain @"
                } else if (DatabaseHelper.userByEmail(email) != null) {
                    emailError = "Email is already using"
                }

                // 2) username checks
                if (username.contains(" ")) {
                    usernameError = "Username must not contain spaces"
                } else if (DatabaseHelper.userByUsername(username) != null) {
                    usernameError = "Username is already taken"
                }

                // 3) password checks
                val lengthOk = password.length in 8..30
                val hasLower = password.any { it.isLowerCase() }
                val hasUpper = password.any { it.isUpperCase() }
                val hasDigit = password.any { it.isDigit() }
                val hasSpecial = password.any { !it.isLetterOrDigit() }

                if (!lengthOk || !hasLower || !hasUpper || !hasDigit || !hasSpecial) {
                    passwordError =
                        "Password must be 8-30 characters, contain upper and lower case letters, numbers and special symbols"
                }

                // 4) confirm password
                if (confirmPassword != password) {
                    confirmPasswordError = "Passwords do not match"
                }

                // 5) terms of service
                if (!agreeTos) {
                    tosError = "Please, agree with Terms of Service"
                }

                val hasAnyError =
                    emailError.isNotEmpty() ||
                            usernameError.isNotEmpty() ||
                            passwordError.isNotEmpty() ||
                            confirmPasswordError.isNotEmpty() ||
                            tosError.isNotEmpty()

                if (!hasAnyError) {
                    // Register user via backend API
                    val userRepository = com.example.ihatemylife.repository.UserRepository(context)
                    
                    // Use coroutine scope for async operation
                    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                        val result = userRepository.registerUser(username, password)
                        
                        result.onSuccess { apiUser ->
                            // Save user locally as well (for backward compatibility)
                            val newUser = User(
                                email = email, // Keep email for local storage
                                username = apiUser.username,
                                password = password
                            )
                            DatabaseHelper.addUser(newUser)
                            
                            // Remember that this user has registered/logged in on this device
                            val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                            prefs.edit()
                                .putBoolean("logged_in", true)
                                .putString("user_identifier", email) // Keep email for compatibility
                                .putString("username", apiUser.username)
                                .putInt("user_id", apiUser.id) // Store backend user ID
                                .apply()

                            // navigate to chats
                            val intent = Intent(context, ChatsActivity::class.java)
                            context.startActivity(intent)
                        }.onFailure { exception ->
                            // Show error - registration failed
                            // For now, fall back to local registration
                            val newUser = User(
                                email = email,
                                username = username,
                                password = password
                            )
                            DatabaseHelper.addUser(newUser)

                            val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                            prefs.edit()
                                .putBoolean("logged_in", true)
                                .putString("user_identifier", newUser.email)
                                .putString("username", newUser.username)
                                .apply()

                            val intent = Intent(context, ChatsActivity::class.java)
                            context.startActivity(intent)
                        }
                    }
                }
            }
        ) {
            Text(text = "Register")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            }
        ) {
            Text(text = "Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = agreeTos,
                onCheckedChange = { agreeTos = it }
            )
            Row {
                Text(text = "I agree on ")
                Text(
                    text = "Terms of Service",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        val intent = Intent(context, TermsActivity::class.java)
                        context.startActivity(intent)
                    }
                )
            }
        }
        if (tosError.isNotEmpty()) {
            Text(
                text = tosError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

