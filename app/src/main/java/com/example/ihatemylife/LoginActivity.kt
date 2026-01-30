package com.example.ihatemylife

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ihatemylife.ui.theme.IhatemylifeTheme
import androidx.lifecycle.lifecycleScope
import com.example.ihatemylife.database.AppDatabase
import com.example.ihatemylife.database.entities.UserEntity
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
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
                    LoginScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("email/login") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                errorMessage = ""
                
                // For now, use DatabaseHelper as fallback
                // TODO: Implement backend authentication when backend adds login endpoint
                val userByEmail = DatabaseHelper.userByEmail(login)
                val userByUsername =
                    if (userByEmail == null) DatabaseHelper.userByUsername(login) else null

                val user = userByEmail ?: userByUsername

                if (user == null || user.password != password) {
                    errorMessage = "Wrong login or password"
                } else {
                    errorMessage = ""

                    // Remember that this user has logged in on this device
                    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    prefs.edit()
                        .putBoolean("logged_in", true)
                        .putString("user_identifier", user.email)
                        .putString("username", user.username)
                        .apply()

                    // Ensure user exists in Room so ChatViewModel can resolve currentUserId and load messages
                    val activity = context as? ComponentActivity
                    activity?.lifecycleScope?.launch {
                        val userDao = AppDatabase.getDatabase(context.applicationContext).userDao()
                        val stableId = user.username.hashCode().and(0x7FFFFFFF).coerceAtLeast(1)
                        userDao.insertUser(UserEntity(id = stableId, username = user.username))
                        val intent = Intent(context, ChatsActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    } ?: run {
                        val intent = Intent(context, ChatsActivity::class.java)
                        context.startActivity(intent)
                    }
                }
            }
        ) {
            Text(text = "Log in")
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val intent = Intent(context, RegisterActivity::class.java)
                context.startActivity(intent)
            }
        ) {
            Text(text = "Register")
        }
    }
}

