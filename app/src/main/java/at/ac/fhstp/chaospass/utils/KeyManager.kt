package at.ac.fhstp.chaospass.utils


import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.biometric.BiometricManager
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

object KeyManager {

    private const val KEY_ALIAS = "password_manager_key"

    fun getOrCreateKey(context: Context): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

        // Check if the key already exists
        val existingKey = keyStore.getKey(KEY_ALIAS, null) as? SecretKey
        if (existingKey != null) return existingKey

        // Check if biometrics are enrolled
        val biometricManager = BiometricManager.from(context)
        val userAuthRequired = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) ==
                BiometricManager.BIOMETRIC_SUCCESS

        // Create a new key
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val keySpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setUserAuthenticationRequired(userAuthRequired)
            .setUserAuthenticationValidityDurationSeconds(-1) // Allows PIN fallback
            .build()

        keyGenerator.init(keySpec)
        return keyGenerator.generateKey()
    }
}
