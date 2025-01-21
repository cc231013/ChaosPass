package at.ac.fhstp.chaospass.utils

import android.util.Log
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec

class EncryptionHelper {
    private val transformation = "AES/GCM/NoPadding"
    private val tagSize = 128 // GCM authentication tag size in bits

    fun logSessionKey() {
        try {
            val key = SessionKeyManager.getKey()
            if (key == null) {
                Log.e("EncryptionHelper", "Session key is null")
            } else {
                Log.d("EncryptionHelper", "Session key algorithm: ${key.algorithm}")
                Log.d("EncryptionHelper", "Session key format: ${key.format}")

                val keyEncoded = key.encoded
                if (keyEncoded != null) {
                    Log.d("EncryptionHelper", "Session key (Base64): ${Base64.getEncoder().encodeToString(keyEncoded)}")
                } else {
                    Log.d("EncryptionHelper", "Session key is hardware-backed and does not expose its raw material")
                }
            }
        } catch (e: Exception) {
            Log.e("EncryptionHelper", "Error logging session key: ${e.message}")
        }
    }

    // Encrypt the provided data using the session key
    fun encrypt(data: String): String {
        logSessionKey()
        Log.d("EncryptionHelper", "Starting encryption for data: $data")

        val secretKey = SessionKeyManager.getKey()
        if (secretKey == null) {
            Log.e("EncryptionHelper", "Encryption failed: Encryption key not available in session")
            throw IllegalStateException("Encryption key not available in session")
        }

        return try {
            Log.d("EncryptionHelper", "Initializing cipher for encryption with transformation: $transformation")
            val cipher = Cipher.getInstance(transformation)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val iv = cipher.iv
            Log.d("EncryptionHelper", "Generated IV: ${Base64.getEncoder().encodeToString(iv)}")

            val encryptedData = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
            Log.d("EncryptionHelper", "Encrypted data length: ${encryptedData.size}")

            val combined = iv + encryptedData
            val result = Base64.getEncoder().encodeToString(combined)
            Log.d("EncryptionHelper", "Combined IV and encrypted data (Base64): $result")

            result
        } catch (e: Exception) {
            Log.e("EncryptionHelper", "Encryption failed: ${e.message}")
            throw e
        }
    }

    // Decrypt the provided data using the session key
    fun decrypt(data: String): String {
        logSessionKey()
        Log.d("EncryptionHelper", "Starting decryption for data: $data")

        val secretKey = SessionKeyManager.getKey()
        if (secretKey == null) {
            Log.e("EncryptionHelper", "Decryption failed: Decryption key not available in session")
            throw IllegalStateException("Decryption key not available in session")
        }

        return try {
            Log.d("EncryptionHelper", "Decoding Base64 encoded data")
            val decoded = Base64.getDecoder().decode(data)
            Log.d("EncryptionHelper", "Decoded data length: ${decoded.size}")

            val ivSize = 12 // GCM recommended IV size
            require(decoded.size > ivSize) { "Invalid data format: Missing IV" }

            val iv = decoded.sliceArray(0 until ivSize)
            val encryptedData = decoded.sliceArray(ivSize until decoded.size)

            Log.d("EncryptionHelper", "Extracted IV: ${Base64.getEncoder().encodeToString(iv)}")
            Log.d("EncryptionHelper", "Encrypted data length: ${encryptedData.size}")

            val cipher = Cipher.getInstance(transformation)
            val spec = GCMParameterSpec(tagSize, iv)
            Log.d("EncryptionHelper", "Initializing cipher for decryption with transformation: $transformation")
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

            val decryptedData = cipher.doFinal(encryptedData)
            val result = String(decryptedData, Charsets.UTF_8)
            Log.d("EncryptionHelper", "Decrypted string: $result")

            result
        } catch (e: Exception) {
            Log.e("EncryptionHelper", "Decryption failed: ${e.message}")
            throw e
        }
    }
}
