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
import at.ac.fhstp.chaospass.utils.SessionKeyManager
import javax.crypto.SecretKey



class MainActivity : FragmentActivity() {
    private var sessionKey: SecretKey? = null // In-memory session key

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val passphrase = "secure-passphrase"
        val database = EntryDatabase.getDatabase(applicationContext, passphrase)

        authenticateUser { success ->
            if (success) {
                val secretKey = KeyManager.getOrCreateKey(this)
                SessionKeyManager.setKey(secretKey) // Store key in the session

                val encryptionHelper = EncryptionHelper()
                val repository = EntryRepository(database.entryDao(), encryptionHelper)

                setContent {
                    val settingsDataStore = SettingsDataStore(this)
                    val settingsRepository = SettingsRepository(settingsDataStore)
                    val settingsViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return SettingsViewModel(settingsRepository) as T
                        }
                    }).get(SettingsViewModel::class.java)

                    val chaosModeEnabled = settingsViewModel.isChaosMode.collectAsState().value
                    ChaosPassTheme(chaosModeEnabled = chaosModeEnabled) {
                        val isAuthenticated = remember { mutableStateOf(true) }

                        // Display app content if authenticated
                        if (isAuthenticated.value) {
                            AppNavGraph(repository = repository, chaosModeEnabled = chaosModeEnabled)
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                finish()
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
            .setDeviceCredentialAllowed(true)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
