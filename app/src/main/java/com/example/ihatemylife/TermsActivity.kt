package com.example.ihatemylife

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ihatemylife.ui.theme.IhatemylifeTheme

class TermsActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val isDarkTheme = prefs.getBoolean("dark_theme", true)
        setContent {
            IhatemylifeTheme(darkTheme = isDarkTheme) {
                TermsScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsScreen() {
    val activity = androidx.compose.ui.platform.LocalContext.current as? ComponentActivity
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Terms of Service") },
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
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Terms of Service",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Last Updated: January 2024",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Service Description
            SectionTitle("1. Service Description")
            SectionText(
                "This messaging application (\"Service\") provides a platform for users to communicate " +
                "through text messages. The Service allows you to send and receive messages, create contacts, " +
                "and manage conversations with other users. By using this Service, you agree to be bound by " +
                "these Terms of Service."
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // User Responsibilities
            SectionTitle("2. User Responsibilities")
            SectionText("You agree to:")
            BulletPoint("Provide accurate and complete information when registering")
            BulletPoint("Maintain the security of your account credentials")
            BulletPoint("Use the Service only for lawful purposes")
            BulletPoint("Not engage in harassment, abuse, or illegal activities")
            BulletPoint("Not share false, misleading, or harmful content")
            BulletPoint("Respect the privacy and rights of other users")
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Privacy Policy
            SectionTitle("3. Privacy and Data Usage")
            SectionText(
                "We are committed to protecting your privacy. The Service collects and processes the following information:"
            )
            BulletPoint("Account information (username, password)")
            BulletPoint("Message content and metadata")
            BulletPoint("Contact information you provide")
            BulletPoint("Device information and usage statistics")
            
            Spacer(modifier = Modifier.height(8.dp))
            
            SectionText(
                "Your messages are stored securely and are only accessible to you and the intended recipients. " +
                "We do not share your personal information with third parties except as required by law or " +
                "to provide the Service. Message content may be processed to deliver messages and improve " +
                "the Service, but we do not use your messages for advertising purposes."
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Account Termination
            SectionTitle("4. Account Termination")
            SectionText(
                "You may terminate your account at any time by contacting support or deleting the application. " +
                "We reserve the right to suspend or terminate your account if you violate these Terms of Service, " +
                "engage in illegal activities, or misuse the Service. Upon termination, your access to the Service " +
                "will be immediately revoked, and we may delete your account data in accordance with our data " +
                "retention policies."
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Limitation of Liability
            SectionTitle("5. Limitation of Liability")
            SectionText(
                "The Service is provided \"as is\" without warranties of any kind. We do not guarantee that the " +
                "Service will be uninterrupted, error-free, or secure. To the maximum extent permitted by law, " +
                "we disclaim all warranties and shall not be liable for any indirect, incidental, special, " +
                "consequential, or punitive damages arising from your use of the Service."
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Intellectual Property
            SectionTitle("6. Intellectual Property")
            SectionText(
                "All content, features, and functionality of the Service are owned by us and are protected by " +
                "copyright, trademark, and other intellectual property laws. You may not copy, modify, distribute, " +
                "or create derivative works based on the Service without our express written permission."
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Changes to Terms
            SectionTitle("7. Changes to Terms")
            SectionText(
                "We reserve the right to modify these Terms of Service at any time. We will notify users of " +
                "significant changes through the Service or by email. Your continued use of the Service after " +
                "such modifications constitutes acceptance of the updated terms."
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Contact Information
            SectionTitle("8. Contact Information")
            SectionText(
                "If you have questions about these Terms of Service, please contact us through the app settings " +
                "or support channels provided within the application."
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun SectionText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun BulletPoint(text: String) {
    Text(
        text = "• $text",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
    )
}

