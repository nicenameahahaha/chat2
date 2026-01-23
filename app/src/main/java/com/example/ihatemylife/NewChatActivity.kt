package com.example.ihatemylife

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.example.ihatemylife.ui.theme.IhatemylifeTheme
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

/**
 * Centralized validation helpers for contact creation.
 * Easily extensible for additional validation rules.
 */
object ContactValidator {
    /**
     * Validates phone number: must be exactly 11 digits (excluding +7 prefix).
     */
    fun validatePhone(phone: String): ValidationResult {
        if (phone.isBlank()) {
            return ValidationResult(false, "Phone number is required")
        }
        // Remove any non-digit characters for validation
        val digitsOnly = phone.filter { it.isDigit() }
        if (digitsOnly.length != 11) {
            return ValidationResult(false, "Phone must contain exactly 11 digits")
        }
        return ValidationResult(true, "")
    }
    
    /**
     * Validates email format and domain.
     * Must not contain spaces, have valid format, and belong to common email domains.
     */
    fun validateEmail(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(false, "Email is required")
        }
        
        if (email.contains(" ")) {
            return ValidationResult(false, "Email must not contain spaces")
        }
        
        // Basic email format validation
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
        if (!emailRegex.matches(email)) {
            return ValidationResult(false, "Invalid email format")
        }
        
        // Check for common email domains
        val commonDomains = listOf(
            "gmail", "yahoo", "outlook", "hotmail", "mail", "yandex",
            "icloud", "protonmail", "aol", "live", "msn", "comcast"
        )
        val domain = email.substringAfter("@").substringBefore(".").lowercase()
        if (!commonDomains.contains(domain)) {
            return ValidationResult(false, "Email must belong to a common email domain (gmail, yahoo, outlook, etc.)")
        }
        
        return ValidationResult(true, "")
    }
    
    /**
     * Validates that at least one contact method (email or phone) is provided and valid.
     */
    fun validateContactMethod(email: String, phone: String): ValidationResult {
        val emailValid = email.isNotBlank() && validateEmail(email).isValid
        val phoneValid = phone.isNotBlank() && validatePhone(phone).isValid
        
        if (!emailValid && !phoneValid) {
            if (email.isBlank() && phone.isBlank()) {
                return ValidationResult(false, "Email or phone number is required")
            }
            if (email.isNotBlank() && !emailValid) {
                return ValidationResult(false, validateEmail(email).errorMessage)
            }
            if (phone.isNotBlank() && !phoneValid) {
                return ValidationResult(false, validatePhone(phone).errorMessage)
            }
        }
        
        return ValidationResult(true, "")
    }
}

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String
)

class NewChatActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IhatemylifeTheme {
                NewChatScreen()
            }
        }
    }
}

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewChatScreen() {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val scope = rememberCoroutineScope()
    
    // Get current user ID
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val currentUserId = prefs.getString("user_identifier", "") ?: ""
    
    // State management
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var sortAscending by remember { mutableStateOf(true) }
    var showNewContactSheet by remember { mutableStateOf(false) }
    var showGroupSelection by remember { mutableStateOf(false) }
    
    // Get all available users/contacts for current user
    val allContacts = DatabaseHelper.getAllAvailableUsers(currentUserId)
    
    // Filter and sort contacts
    val filteredContacts = if (searchQuery.isBlank()) {
        allContacts
    } else {
        DatabaseHelper.searchContacts(currentUserId, searchQuery)
    }
    
    val sortedContacts = if (sortAscending) {
        filteredContacts.sortedBy { "${it.firstName} ${it.lastName}".trim() }
    } else {
        filteredContacts.sortedByDescending { "${it.firstName} ${it.lastName}".trim() }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    if (isSearchActive) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search by email, phone, username") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(
                                        onClick = { 
                                            searchQuery = ""
                                            isSearchActive = false
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Clear search"
                                        )
                                    }
                                }
                            }
                        )
                    } else {
                        Text("New Chat")
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (isSearchActive) {
                                isSearchActive = false
                                searchQuery = ""
                            } else {
                                activity?.finish()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (!isSearchActive) {
                        IconButton(
                            onClick = { isSearchActive = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                    }
                    Text(
                        text = if (sortAscending) "A-Z" else "Z-A",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { sortAscending = !sortAscending }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Action buttons: New Group and New Contact
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { showGroupSelection = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("New Group")
                }
                Button(
                    onClick = { showNewContactSheet = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("New Contact")
                }
            }
            
            // Active users list
            // LazyColumn automatically handles dynamic layout and scrollability
            // Each contact item has proper spacing and will shift appropriately as new contacts are added
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(sortedContacts) { contact ->
                    ContactItem(
                        contact = contact,
                        onClick = {
                            // Close search if active
                            if (isSearchActive) {
                                isSearchActive = false
                                searchQuery = ""
                            }
                            
                            // Navigate to ChatActivity for this contact
                            // Check if chat already exists, if not create one
                            val existingChat = DatabaseHelper.getActiveChats().find { 
                                it.title == "${contact.firstName} ${contact.lastName}".trim() ||
                                it.id == contact.id
                            }
                            
                            val chatId = existingChat?.id ?: "chat_${System.currentTimeMillis()}"
                            val chatTitle = "${contact.firstName} ${contact.lastName}".trim().ifEmpty { 
                                contact.email ?: contact.phone ?: "Contact"
                            }
                            
                            // Create chat if it doesn't exist
                            if (existingChat == null) {
                                val newChat = Chat(
                                    id = chatId,
                                    title = chatTitle,
                                    lastMessage = "Chat started",
                                    isActive = true
                                )
                                DatabaseHelper.addChat(newChat)
                            }
                            
                            // Navigate to ChatActivity
                            val intent = Intent(context, ChatActivity::class.java).apply {
                                putExtra("chat_id", chatId)
                                putExtra("chat_title", chatTitle)
                                putExtra("contact_id", contact.id)
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
    
    // New Contact Bottom Sheet
    if (showNewContactSheet) {
        NewContactBottomSheet(
            onDismiss = { showNewContactSheet = false },
            onCreateContact = { contact ->
                // Add contact for current user (user-scoped) with duplicate check
                val (success, errorMessage) = DatabaseHelper.addContact(currentUserId, contact)
                
                if (success) {
                    // Create a chat with this contact
                    val chat = Chat(
                        id = "chat_${System.currentTimeMillis()}",
                        title = "${contact.firstName} ${contact.lastName}".trim(),
                        lastMessage = "Chat started",
                        isActive = true
                    )
                    DatabaseHelper.addChat(chat)
                    // Return success - sheet will close
                    Pair(true, "")
                } else {
                    // Return error - sheet will show duplicate error
                    Pair(false, errorMessage)
                }
            }
        )
    }
    
    // Group Selection Screen
    if (showGroupSelection) {
        GroupSelectionScreen(
            onDismiss = { showGroupSelection = false },
            onCreateGroup = { title, participantIds ->
                DatabaseHelper.createGroupChat(title, participantIds)
                showGroupSelection = false
            }
        )
    }
}

@Composable
fun ContactItem(
    contact: Contact,
    onClick: () -> Unit
) {
    // Contact item design consistent with ChatsActivity chat items
    // Padding is handled by LazyColumn contentPadding for proper spacing
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        Color.Gray.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contact.firstName.take(1).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${contact.firstName} ${contact.lastName}".trim(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (contact.email != null) {
                    Text(
                        text = contact.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                if (contact.phone != null) {
                    Text(
                        text = contact.phone,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewContactBottomSheet(
    onDismiss: () -> Unit,
    onCreateContact: (Contact) -> Pair<Boolean, String> // Returns (success, errorMessage)
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    
    var firstNameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }
    var contactMethodError by remember { mutableStateOf("") }
    var duplicateError by remember { mutableStateOf("") }
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "New Contact",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            OutlinedTextField(
                value = firstName,
                onValueChange = { 
                    firstName = it
                    firstNameError = ""
                },
                label = { Text("First Name *") },
                singleLine = true,
                isError = firstNameError.isNotEmpty(),
                supportingText = if (firstNameError.isNotEmpty()) {
                    { Text(firstNameError) }
                } else null
            )
            
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                singleLine = true
            )
            
            OutlinedTextField(
                value = email,
                onValueChange = { 
                    email = it
                    emailError = ""
                    contactMethodError = ""
                },
                label = { Text("Email") },
                singleLine = true,
                isError = emailError.isNotEmpty(),
                supportingText = if (emailError.isNotEmpty()) {
                    { Text(emailError) }
                } else null
            )
            
            // Phone field with +7 prefix
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Static +7 prefix label
                Text(
                    text = "+7",
                    modifier = Modifier.padding(end = 8.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                // Phone input (only digits, max 11)
                OutlinedTextField(
                    value = phone,
                    onValueChange = { newValue ->
                        // Only allow digits, max 11 characters
                        val digitsOnly = newValue.filter { it.isDigit() }
                        if (digitsOnly.length <= 11) {
                            phone = digitsOnly
                            phoneError = ""
                            contactMethodError = ""
                        }
                    },
                    label = { Text("Phone Number (11 digits)") },
                    singleLine = true,
                    isError = phoneError.isNotEmpty(),
                    supportingText = if (phoneError.isNotEmpty()) {
                        { Text(phoneError) }
                    } else null,
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    )
                )
            }
            
            if (contactMethodError.isNotEmpty()) {
                Text(
                    text = contactMethodError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            if (duplicateError.isNotEmpty()) {
                Text(
                    text = duplicateError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = {
                    // Clear previous errors
                    firstNameError = ""
                    emailError = ""
                    phoneError = ""
                    contactMethodError = ""
                    
                    // Validate first name
                    if (firstName.isBlank()) {
                        firstNameError = "First name is required"
                        return@Button
                    }
                    
                    // Validate contact method (email or phone)
                    val emailResult = if (email.isNotBlank()) ContactValidator.validateEmail(email) else ValidationResult(true, "")
                    val phoneResult = if (phone.isNotBlank()) ContactValidator.validatePhone(phone) else ValidationResult(true, "")
                    
                    if (email.isNotBlank() && !emailResult.isValid) {
                        emailError = emailResult.errorMessage
                    }
                    
                    if (phone.isNotBlank() && !phoneResult.isValid) {
                        phoneError = phoneResult.errorMessage
                    }
                    
                    // Check if at least one valid contact method is provided
                    val contactMethodResult = ContactValidator.validateContactMethod(email, phone)
                    if (!contactMethodResult.isValid) {
                        contactMethodError = contactMethodResult.errorMessage
                        // Also set individual errors if fields are invalid
                        if (email.isNotBlank() && !emailResult.isValid) {
                            emailError = emailResult.errorMessage
                        }
                        if (phone.isNotBlank() && !phoneResult.isValid) {
                            phoneError = phoneResult.errorMessage
                        }
                        return@Button
                    }
                    
                    // Create contact with validated data
                    val contact = Contact(
                        id = "contact_${System.currentTimeMillis()}",
                        firstName = firstName.trim(),
                        lastName = lastName.trim(),
                        email = if (email.isNotBlank() && emailResult.isValid) email.trim() else null,
                        phone = if (phone.isNotBlank() && phoneResult.isValid) "+7$phone" else null // Add +7 prefix
                    )
                    
                    // Try to create contact (includes duplicate check)
                    val (success, errorMsg) = onCreateContact(contact)
                    if (success) {
                        // Success - close sheet
                        onDismiss()
                    } else {
                        // Show duplicate error
                        duplicateError = errorMsg
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Contact")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupSelectionScreen(
    onDismiss: () -> Unit,
    onCreateGroup: (String, List<String>) -> Unit
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val currentUserEmail = prefs.getString("user_identifier", "") ?: ""
    
    var selectedContactIds by remember { mutableStateOf(setOf<String>()) }
    // Get contacts for current user
    val allContacts = DatabaseHelper.getAllAvailableUsers(currentUserEmail)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Contacts for Group") },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Instructions
            Text(
                text = "Select 2-9 contacts (minimum 3 total including you)",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            
            // Contact selection list
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(allContacts.filter { it.id != currentUserEmail }) { contact ->
                    SelectableContactItem(
                        contact = contact,
                        isSelected = selectedContactIds.contains(contact.id),
                        onToggle = {
                            if (selectedContactIds.contains(contact.id)) {
                                selectedContactIds = selectedContactIds - contact.id
                            } else {
                                selectedContactIds = selectedContactIds + contact.id
                            }
                        }
                    )
                }
            }
            
            // Create Group button (shown when 2+ contacts selected)
            if (selectedContactIds.size >= 2) {
                val totalParticipants = selectedContactIds.size + 1 // +1 for current user
                if (totalParticipants in 3..10) {
                    Button(
                        onClick = {
                            val groupTitle = "Group Chat (${totalParticipants} members)"
                            val participantIds = listOf(currentUserEmail) + selectedContactIds.toList()
                            onCreateGroup(groupTitle, participantIds)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Create Group")
                    }
                } else {
                    Text(
                        text = "Group must have 3-10 members total",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun SelectableContactItem(
    contact: Contact,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onToggle),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Selection indicator
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Avatar placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        Color.Gray.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contact.firstName.take(1).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${contact.firstName} ${contact.lastName}".trim(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (contact.email != null) {
                    Text(
                        text = contact.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}
