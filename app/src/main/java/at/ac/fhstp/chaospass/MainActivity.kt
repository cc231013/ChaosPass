package at.ac.fhstp.chaospass

import SettingsDataStore
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import at.ac.fhstp.chaospass.data.database.EntryDatabase
import at.ac.fhstp.chaospass.data.repository.EntryRepository
import at.ac.fhstp.chaospass.data.repository.SettingsRepository
import at.ac.fhstp.chaospass.ui.AppNavGraph
import at.ac.fhstp.chaospass.ui.theme.ChaosPassTheme
import at.ac.fhstp.chaospass.ui.viewmodel.SettingsViewModel
import at.ac.fhstp.chaospass.utils.EncryptionHelper
import at.ac.fhstp.chaospass.utils.KeyManager


class MainActivity : FragmentActivity() {
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize dependencies
        val passphrase = "secure-passphrase" // Securely generate/retrieve this
        val database = EntryDatabase.getDatabase(applicationContext, passphrase)
        val secretKey = KeyManager.getOrCreateKey(this)
        val encryptionHelper = EncryptionHelper(secretKey)
        val repository = EntryRepository(database.entryDao(), encryptionHelper)

        // Initialize SettingsDataStore and SettingsRepository
        val settingsDataStore = SettingsDataStore(this)
        val settingsRepository = SettingsRepository(settingsDataStore)
        val settingsViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SettingsViewModel(settingsRepository) as T
            }
        }).get(SettingsViewModel::class.java)

        setContent {
            // Observe chaos mode state
            val chaosModeEnabled = settingsViewModel.isChaosMode.collectAsState().value

            ChaosPassTheme(chaosModeEnabled = chaosModeEnabled) {
                val isAuthenticated = remember { mutableStateOf(false) }

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
                    AppNavGraph(repository = repository, chaosModeEnabled = chaosModeEnabled)
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
            .setSubtitle("Use biometrics or device PIN to access ChaosPass")
            .setDeviceCredentialAllowed(true) // Allows PIN, pattern, or password as a fallback
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
