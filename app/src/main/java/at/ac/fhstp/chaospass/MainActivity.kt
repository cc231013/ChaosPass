package at.ac.fhstp.chaospass

import at.ac.fhstp.chaospass.utils.EncryptionHelper
import SettingsDataStore
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import at.ac.fhstp.chaospass.utils.KeyManager
import kotlinx.coroutines.runBlocking
import javax.crypto.SecretKey

class MainActivity : FragmentActivity() {
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize dependencies
        val passphrase = "secure-passphrase" // Securely generate/retrieve this
        val database = EntryDatabase.getDatabase(applicationContext, passphrase)

        // Declare secretKey as a mutable variable
        var secretKey: SecretKey? = null

        // Validate or recreate the key, and handle re-encryption if necessary
        secretKey = KeyManager.getOrCreateKey(this) { newKey ->
            Log.d("MainActivity", "Key rotation detected. Re-encrypting data with new key...")

            // Initialize the EncryptionHelper for the old and new keys
            val oldHelper = secretKey?.let { EncryptionHelper(it) }
            val newHelper = EncryptionHelper(newKey)

            // Re-encrypt data in the database
            oldHelper?.let { oldEncryptionHelper ->
                val repository = EntryRepository(database.entryDao(), oldEncryptionHelper)
                runBlocking {
                    repository.reEncryptAllEntries(newHelper)
                }
            }

            Log.d("MainActivity", "Re-encryption complete.")
            secretKey = newKey // Update the secretKey reference to the new key
        }

        // Create EncryptionHelper with the secret key
        val encryptionHelper = EncryptionHelper(secretKey!!)
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

        // Set up the app UI
        setContent {
            val chaosModeEnabled = settingsViewModel.isChaosMode.collectAsState().value
            ChaosPassTheme(chaosModeEnabled = chaosModeEnabled) {
                val isAuthenticated = remember { mutableStateOf(false) }

                if (!isAuthenticated.value) {
                    authenticateUser { success ->
                        if (success) {
                            isAuthenticated.value = true
                        } else {
                            Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                            finish() // Exit the app if authentication fails
                        }
                    }
                } else {
                    AppNavGraph(repository = repository, chaosModeEnabled = chaosModeEnabled)
                }
            }
        }
    }

    private fun authenticateUser(onResult: (Boolean) -> Unit) {
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
            .setDeviceCredentialAllowed(true) // Allows PIN, pattern, or password as fallback
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
