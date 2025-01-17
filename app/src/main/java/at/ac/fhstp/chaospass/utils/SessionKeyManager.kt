package at.ac.fhstp.chaospass.utils

import javax.crypto.SecretKey


object SessionKeyManager {
    private var sessionKey: SecretKey? = null

    // Set the session key after successful authentication
    fun setKey(key: SecretKey) {
        sessionKey = key
    }

    // Retrieve the session key for encryption/decryption
    fun getKey(): SecretKey? {
        return sessionKey
    }

    // Clear the session key when the app is closed or the session ends
    fun clearKey() {
        sessionKey = null
    }
}
