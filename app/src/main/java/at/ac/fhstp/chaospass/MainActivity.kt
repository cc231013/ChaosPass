package at.ac.fhstp.chaospass

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import at.ac.fhstp.chaospass.data.database.EntryDatabase
import at.ac.fhstp.chaospass.data.repository.EntryRepository
import at.ac.fhstp.chaospass.ui.AppNavGraph
import at.ac.fhstp.chaospass.ui.theme.ChaosPassTheme // Import your custom theme
import at.ac.fhstp.chaospass.ui.viewmodel.factory.EntryViewModelFactory
import at.ac.fhstp.chaospass.utils.EncryptionHelper
import at.ac.fhstp.chaospass.utils.KeyManager
import at.ac.fhstp.chaospass.viewmodel.EntryViewModel

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize dependencies
        val passphrase = "secure-passphrase" // Securely generate/retrieve this
        val database = EntryDatabase.getDatabase(applicationContext, passphrase)
        val secretKey = KeyManager.getOrCreateKey(this)
        val encryptionHelper = EncryptionHelper(secretKey)
        val repository = EntryRepository(database.entryDao(), encryptionHelper)
        val viewModelFactory = EntryViewModelFactory(repository)

        setContent {
            ChaosPassTheme { // Wrap the composables with your theme
                val isAuthenticated = remember { mutableStateOf(false) }
                val coroutineScope = rememberCoroutineScope()

                if (!isAuthenticated.value) {
                    // Trigger authentication when the app is launched
                    AuthenticateUser { success ->
                        if (success) {
                            isAuthenticated.value = true
                        } else {
                            Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                            finish() // Exit the app if authentication fails
                        }
                    }
                } else {
                    // Show main app content after successful authentication
                    val viewModel: EntryViewModel = viewModel(factory = viewModelFactory)
                    AppNavGraph(repository = repository)
                }
            }
        }
    }

    private fun AuthenticateUser(onResult: (Boolean) -> Unit) {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(
            this,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onResult(true)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onResult(false)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onResult(false)
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authenticate")
            .setSubtitle("Use biometrics or device PIN to access your password vault")
            .setDeviceCredentialAllowed(true) // Allows PIN, pattern, or password as a fallback
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
