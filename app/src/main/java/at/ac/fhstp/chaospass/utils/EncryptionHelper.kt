package at.ac.fhstp.chaospass.utils

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import java.util.Base64

class EncryptionHelper(private val secretKey: SecretKey) {

    private val transformation = "AES/GCM/NoPadding"
    private val ivSize = 12 // GCM recommended IV size
    private val tagSize = 128 // GCM authentication tag size

    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv // System-generated IV
        val encryptedData = cipher.doFinal(data.toByteArray())

        // Combine IV and encrypted data
        val combined = iv + encryptedData
        return Base64.getEncoder().encodeToString(combined)
    }

    fun decrypt(data: String): String {
        val decoded = Base64.getDecoder().decode(data)
        val iv = decoded.sliceArray(0 until ivSize)
        val encryptedData = decoded.sliceArray(ivSize until decoded.size)

        val cipher = Cipher.getInstance(transformation)
        val spec = GCMParameterSpec(tagSize, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

        return String(cipher.doFinal(encryptedData))
    }
}
