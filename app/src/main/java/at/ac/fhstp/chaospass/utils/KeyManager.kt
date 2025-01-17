package at.ac.fhstp.chaospass.utils

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey


object KeyManager {

    private const val KEY_ALIAS = "password_manager_key"

    fun getOrCreateKey(context: Context, onKeyRotation: ((SecretKey) -> Unit)? = null): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

        // Check if the key already exists
        val existingKey = keyStore.getKey(KEY_ALIAS, null) as? SecretKey
        if (existingKey != null) {
            Log.d("KeyManager", "Existing key found")
            return existingKey
        }

        // If key doesn't exist or rotation is required, create a new key
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
        val newKey = keyGenerator.generateKey()

        // Invoke the callback if provided
        onKeyRotation?.invoke(newKey)

        return newKey
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
        return keyStore.getKey(KEY_ALIAS, null) != null
    }
}
