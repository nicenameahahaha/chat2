package com.example.ihatemylife

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.ihatemylife.ui.theme.IhatemylifeTheme
import java.io.File
import java.io.FileOutputStream

class MyProfileActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
        val isDarkTheme = prefs.getBoolean("dark_theme", true)
        setContent {
            IhatemylifeTheme(darkTheme = isDarkTheme) {
                MyProfileScreen()
            }
        }
    }
}

private const val FIELD_PHONE = "phone"
private const val FIELD_EMAIL = "email"
private const val FIELD_USERNAME = "username"
private const val AVATAR_PATH_KEY = "avatar_path"
private const val AVATAR_FILE_NAME = "avatar.jpg"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen() {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    var avatarPath by remember { mutableStateOf(prefs.getString(AVATAR_PATH_KEY, null)) }
    val currentPhone = prefs.getString("phone", "") ?: ""
    val currentEmail = prefs.getString("user_identifier", "") ?: ""
    val currentUsername = prefs.getString("username", "") ?: ""

    var editingField by remember { mutableStateOf<String?>(null) }
    var showVerificationPlaceholder by remember { mutableStateOf(false) }
    var fieldError by remember { mutableStateOf("") }
    var showAvatarMenu by remember { mutableStateOf(false) }
    var pendingAvatarPath by remember { mutableStateOf<String?>(null) }
    var showRevertDialog by remember { mutableStateOf(false) }

    val avatarDir = remember { File(context.filesDir, "avatars").apply { mkdirs() } }
    val captureFile = remember { File(avatarDir, "capture_${System.currentTimeMillis()}.jpg") }

    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && captureFile.exists()) {
            pendingAvatarPath = captureFile.absolutePath
        } else {
            showAvatarMenu = false
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val dest = File(avatarDir, "picked_${System.currentTimeMillis()}.jpg")
                context.contentResolver.openInputStream(uri)?.use { input ->
                    FileOutputStream(dest).use { output ->
                        input.copyTo(output)
                    }
                }
                if (dest.exists()) pendingAvatarPath = dest.absolutePath
                else showAvatarMenu = false
            } catch (_: Exception) {
                showAvatarMenu = false
            }
        } else {
            showAvatarMenu = false
        }
    }

    if (showVerificationPlaceholder) {
        VerificationPlaceholderScreen(
            onBack = { showVerificationPlaceholder = false }
        )
        return
    }

    if (showRevertDialog) {
        AlertDialog(
            onDismissRequest = { showRevertDialog = false },
            title = { Text("Revert changes?") },
            text = { Text("Discard avatar change and restore previous?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        pendingAvatarPath = null
                        showRevertDialog = false
                    }
                ) {
                    Text("Revert", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRevertDialog = false }) {
                    Text("Continue editing")
                }
            }
        )
    }

    BackHandler(enabled = pendingAvatarPath != null) {
        showRevertDialog = true
    }

    when (val field = editingField) {
        null -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("My Profile") },
                        navigationIcon = {
                            IconButton(onClick = {
                                if (pendingAvatarPath != null) showRevertDialog = true
                                else activity?.finish()
                            }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        }
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val pathToShow = pendingAvatarPath ?: avatarPath
                        Box(
                            modifier = Modifier
                                .size(200.dp)
                                .clip(CircleShape)
                                .background(
                                    if (pathToShow != null) Color.Transparent
                                    else MaterialTheme.colorScheme.primaryContainer,
                                    shape = CircleShape
                                )
                                .clickable { showAvatarMenu = true },
                            contentAlignment = Alignment.Center
                        ) {
                            if (pathToShow != null) {
                                val bitmap = remember(pathToShow) {
                                    BitmapFactory.decodeFile(pathToShow)?.asImageBitmap()
                                }
                                if (bitmap != null) {
                                    Image(
                                        bitmap = bitmap,
                                        contentDescription = "Avatar",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Text(
                                        text = currentUsername.take(1).uppercase().ifEmpty { "?" },
                                        style = MaterialTheme.typography.displayMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            } else {
                                Text(
                                    text = currentUsername.take(1).uppercase().ifEmpty { "?" },
                                    style = MaterialTheme.typography.displayMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                    if (pendingAvatarPath != null) {
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(
                                onClick = {
                                    val dest = File(avatarDir, AVATAR_FILE_NAME)
                                    pendingAvatarPath?.let { src ->
                                        File(src).copyTo(dest, overwrite = true)
                                        prefs.edit().putString(AVATAR_PATH_KEY, dest.absolutePath).apply()
                                        avatarPath = dest.absolutePath
                                    }
                                    pendingAvatarPath = null
                                },
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Confirm",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    ProfileFieldRow(
                        label = "Phone",
                        value = currentPhone.ifEmpty { "Not set" },
                        onClick = { editingField = FIELD_PHONE }
                    )
                    ProfileFieldRow(
                        label = "Email",
                        value = currentEmail.ifEmpty { "Not set" },
                        onClick = { editingField = FIELD_EMAIL }
                    )
                    ProfileFieldRow(
                        label = "Username",
                        value = currentUsername.ifEmpty { "Not set" },
                        onClick = { editingField = FIELD_USERNAME }
                    )
                }
            }

            if (showAvatarMenu) {
                val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                ModalBottomSheet(
                    onDismissRequest = { showAvatarMenu = false },
                    sheetState = sheetState
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TextButton(
                            onClick = {
                                showAvatarMenu = false
                                val uri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.fileprovider",
                                    captureFile
                                )
                                takePictureLauncher.launch(uri)
                            }
                        ) {
                            Text("Take photo")
                        }
                        TextButton(
                            onClick = {
                                showAvatarMenu = false
                                pickImageLauncher.launch("image/*")
                            }
                        ) {
                            Text("Choose from gallery")
                        }
                    }
                }
            }
        }
        FIELD_PHONE, FIELD_EMAIL, FIELD_USERNAME -> {
            val currentValue = when (field) {
                FIELD_PHONE -> currentPhone
                FIELD_EMAIL -> currentEmail
                FIELD_USERNAME -> currentUsername
                else -> ""
            }
            EditFieldScreen(
                fieldLabel = field.replaceFirstChar { it.uppercase() },
                currentValue = currentValue,
                error = fieldError,
                onBack = {
                    editingField = null
                    fieldError = ""
                },
                onConfirm = { newValue ->
                    when (field) {
                        FIELD_PHONE -> {
                            prefs.edit().putString("phone", newValue).apply()
                            editingField = null
                            showVerificationPlaceholder = true
                        }
                        FIELD_USERNAME -> {
                            val existing = DatabaseHelper.userByUsername(newValue)
                            if (existing != null && existing.email != currentEmail) {
                                fieldError = "Username is already in use"
                            } else {
                                prefs.edit().putString("username", newValue).apply()
                                editingField = null
                                fieldError = ""
                            }
                        }
                        FIELD_EMAIL -> {
                            val existing = DatabaseHelper.userByEmail(newValue)
                            if (existing != null && existing.username != currentUsername) {
                                fieldError = "Email is already in use"
                            } else {
                                prefs.edit().putString("user_identifier", newValue).apply()
                                editingField = null
                                fieldError = ""
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun ProfileFieldRow(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditFieldScreen(
    fieldLabel: String,
    currentValue: String,
    error: String,
    onBack: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember(currentValue) { mutableStateOf(currentValue) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit $fieldLabel") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(fieldLabel) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = fieldLabel != "Phone",
                    isError = error.isNotEmpty(),
                    supportingText = if (error.isNotEmpty()) { { Text(error) } } else null
                )
            }
            IconButton(
                onClick = { onConfirm(text.trim()) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
                    .size(56.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Confirm",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VerificationPlaceholderScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Verification") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Work In Progress!",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}
