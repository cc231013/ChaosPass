package at.ac.fhstp.chaospass.utils

import android.util.Log
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec


class EncryptionHelper(private val secretKey: SecretKey) {

    private val transformation = "AES/GCM/NoPadding"
    private val tagSize = 128 // GCM authentication tag size in bits

    fun encrypt(data: String): String {
        require(data.isNotEmpty()) { "Data to encrypt cannot be empty" }
        Log.d("at.ac.fhstp.chaospass.utils.EncryptionHelper", "Starting encryption for data: $data")

        try {
            val cipher = Cipher.getInstance(transformation)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val iv = cipher.iv
            val encryptedData = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
            val combined = iv + encryptedData
            return Base64.getEncoder().encodeToString(combined)
        } catch (e: Exception) {
            Log.e("at.ac.fhstp.chaospass.utils.EncryptionHelper", "Encryption failed: ${e.message}")
            throw e
        }
    }

    fun decrypt(data: String): String {
        require(data.isNotEmpty()) { "Data to decrypt cannot be empty" }
        Log.d("at.ac.fhstp.chaospass.utils.EncryptionHelper", "Starting decryption for data: $data")

        try {
            val decoded = Base64.getDecoder().decode(data)
            val ivSize = 12 // GCM recommended IV size
            require(decoded.size > ivSize) { "Invalid data format: Missing IV" }

            val iv = decoded.sliceArray(0 until ivSize)
            val encryptedData = decoded.sliceArray(ivSize until decoded.size)

            val cipher = Cipher.getInstance(transformation)
            val spec = GCMParameterSpec(tagSize, iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

            return String(cipher.doFinal(encryptedData), Charsets.UTF_8)
        } catch (e: Exception) {
            Log.e("at.ac.fhstp.chaospass.utils.EncryptionHelper", "Decryption failed: ${e.message}")
            throw e
        }
    }

    fun reEncryptData(data: String, oldKey: SecretKey, newKey: SecretKey): String {
        Log.d("at.ac.fhstp.chaospass.utils.EncryptionHelper", "Re-encrypting data with a new key...")

        try {
            // Decrypt data with the old key
            val oldHelper = EncryptionHelper(oldKey)
            val decryptedData = oldHelper.decrypt(data)

            // Encrypt data with the new key
            val newHelper = EncryptionHelper(newKey)
            return newHelper.encrypt(decryptedData)
        } catch (e: Exception) {
            Log.e("at.ac.fhstp.chaospass.utils.EncryptionHelper", "Re-encryption failed: ${e.message}")
            throw e
        }
    }
}
