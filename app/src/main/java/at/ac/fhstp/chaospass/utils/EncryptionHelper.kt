package at.ac.fhstp.chaospass.utils

import android.util.Log
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec


class EncryptionHelper {
    private val transformation = "AES/GCM/NoPadding"
    private val tagSize = 128 // GCM authentication tag size in bits

    // Encrypt the provided data using the session key
    fun encrypt(data: String): String {
        val secretKey = SessionKeyManager.getKey()
            ?: throw IllegalStateException("Encryption key not available in session")

        return try {
            val cipher = Cipher.getInstance(transformation)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val iv = cipher.iv
            val encryptedData = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
            val combined = iv + encryptedData
            Base64.getEncoder().encodeToString(combined)
        } catch (e: Exception) {
            Log.e("EncryptionHelper", "Encryption failed: ${e.message}")
            throw e
        }
    }

    // Decrypt the provided data using the session key
    fun decrypt(data: String): String {
        val secretKey = SessionKeyManager.getKey()
            ?: throw IllegalStateException("Decryption key not available in session")

        return try {
            val decoded = Base64.getDecoder().decode(data)
            val ivSize = 12 // GCM recommended IV size
            require(decoded.size > ivSize) { "Invalid data format: Missing IV" }

            val iv = decoded.sliceArray(0 until ivSize)
            val encryptedData = decoded.sliceArray(ivSize until decoded.size)

            val cipher = Cipher.getInstance(transformation)
            val spec = GCMParameterSpec(tagSize, iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

            String(cipher.doFinal(encryptedData), Charsets.UTF_8)
        } catch (e: Exception) {
            Log.e("EncryptionHelper", "Decryption failed: ${e.message}")
            throw e
        }
    }
}
