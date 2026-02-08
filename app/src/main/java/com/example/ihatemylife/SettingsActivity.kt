package com.example.ihatemylife

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ihatemylife.ui.theme.IhatemylifeTheme
import com.example.ihatemylife.viewmodel.SettingsViewModel

class SettingsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val application = applicationContext as? Application
            if (application == null) return@setContent

            val viewModel: SettingsViewModel = viewModel(
                factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return SettingsViewModel(application) as T
                    }
                }
            )

            val isDarkTheme by viewModel.isDarkTheme.collectAsState()

            IhatemylifeTheme(darkTheme = isDarkTheme) {
                SettingsScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val scope = rememberCoroutineScope()
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Appearance Section
            SettingsSection(title = "Appearance") {
                SettingItem(
                    title = "Dark Theme",
                    description = "Enable dark mode",
                    action = {
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { viewModel.setDarkTheme(it) }
                        )
                    }
                )
            }

            // Data Section
            SettingsSection(title = "Data") {
                SettingItem(
                    title = "Clear all contacts and users",
                    description = "Remove all added contacts and registered users from this device",
                    action = {
                        Button(
                            onClick = {
                                scope.launch {
                                    DataCleanup.clearAllContactsAndUsers(context)
                                    Toast.makeText(context, "All contacts and users cleared", Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) {
                            Text("Clear all")
                        }
                    }
                )
            }

            // Account Section
            SettingsSection(title = "Account") {
                SettingItem(
                    title = "Log out",
                    description = "Sign out and return to registration",
                    action = {
                        Button(
                            onClick = {
                                activity?.let { SessionHelper.logoutAndNavigateToRegister(it) }
                            }
                        ) {
                            Text("Log out")
                        }
                    }
                )
            }

            // About Section (reduced spacing)
            SettingsSection(title = "About", reducedSpacing = true) {
                SettingItem(
                    title = "App Version",
                    description = "Current application version",
                    action = {
                        Text(
                            text = "1.0.0",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    reducedSpacing: Boolean = false,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(if (reducedSpacing) 4.dp else 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = if (reducedSpacing) 4.dp else 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(if (reducedSpacing) 12.dp else 16.dp),
                verticalArrangement = Arrangement.spacedBy(if (reducedSpacing) 8.dp else 12.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun SettingItem(
    title: String,
    description: String,
    action: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        Spacer(modifier = Modifier.padding(horizontal = 16.dp))
        action()
    }
}
