import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.SecretKeySpec
import android.security.keystore.KeyInfo

object KeyManager {

    private const val KEY_ALIAS = "password_manager_key"

    fun getOrCreateKey(context: Context): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

        // Check if the key already exists
        val existingKey = keyStore.getKey(KEY_ALIAS, null) as? SecretKey
        if (existingKey != null) {
            logKeyDetails(existingKey)
            return existingKey
        }

        // Create a new key
        Log.d("KeyManager", "Creating a new key...")
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val keySpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setUserAuthenticationRequired(true)
            .setUserAuthenticationValidityDurationSeconds(8 * 60 * 60) // Valid for 8 hours
            .build()

        keyGenerator.init(keySpec)
        return keyGenerator.generateKey()
    }

    fun deleteKey() {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        if (keyStore.containsAlias(KEY_ALIAS)) {
            keyStore.deleteEntry(KEY_ALIAS)
            Log.d("KeyManager", "Key deleted.")
        }
    }

    fun isKeyValid(): Boolean {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        val key = keyStore.getKey(KEY_ALIAS, null) ?: return false
        val factory = SecretKeyFactory.getInstance(key.algorithm, "AndroidKeyStore")
        val keyInfo = factory.getKeySpec(key as SecretKey?, KeyInfo::class.java) as KeyInfo
        return keyInfo.userAuthenticationValidityDurationSeconds > 0
    }

    private fun logKeyDetails(secretKey: SecretKey) {
        try {
            val factory = SecretKeyFactory.getInstance(secretKey.algorithm, "AndroidKeyStore")
            val keyInfo = factory.getKeySpec(secretKey, KeyInfo::class.java) as KeyInfo
            Log.d("KeyManager", "Key Algorithm: ${secretKey.algorithm}")
            Log.d("KeyManager", "Inside Secure Hardware: ${keyInfo.isInsideSecureHardware}")
            Log.d("KeyManager", "User Authentication Required: ${keyInfo.isUserAuthenticationRequired}")
            Log.d("KeyManager", "Authentication Validity Duration: ${keyInfo.userAuthenticationValidityDurationSeconds}")
        } catch (e: Exception) {
            Log.e("KeyManager", "Failed to log key details: ${e.message}")
        }
    }
}
