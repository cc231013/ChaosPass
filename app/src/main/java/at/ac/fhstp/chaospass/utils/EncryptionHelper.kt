import android.security.keystore.KeyInfo
import android.util.Log
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionHelper(private val secretKey: SecretKey) {

    private val transformation = "AES/GCM/NoPadding"
    private val tagSize = 128 // GCM authentication tag size in bits

    fun encrypt(data: String): String {
        require(data.isNotEmpty()) { "Data to encrypt cannot be empty" }
        Log.d("EncryptionHelper", "Starting encryption for data: $data")

        try {
            val cipher = Cipher.getInstance(transformation)
            Log.d("EncryptionHelper", "Cipher initialized with transformation: $transformation")
            logKeyInfo()
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            Log.d("EncryptionHelper", "Cipher initialized for encryption")
            logKeyInfo()
            val iv = cipher.iv
            Log.d("EncryptionHelper", "Generated IV (Base64): ${Base64.getEncoder().encodeToString(iv)}")
            logKeyInfo()
            val encryptedData = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
            Log.d("EncryptionHelper", "Encrypted Data (Base64): ${Base64.getEncoder().encodeToString(encryptedData)}")
            logKeyInfo()
            val combined = iv + encryptedData
            val combinedBase64 = Base64.getEncoder().encodeToString(combined)
            Log.d("EncryptionHelper", "Combined IV and Encrypted Data (Base64): $combinedBase64")
            logKeyInfo()
            return combinedBase64
        } catch (e: Exception) {
            Log.e("EncryptionHelper", "Encryption failed: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }


    fun decrypt(data: String): String {
        require(data.isNotEmpty()) { "Data to decrypt cannot be empty" }
        Log.d("EncryptionHelper", "Starting decryption for data: $data")

        try {
            val decoded = Base64.getDecoder().decode(data)
            Log.d("EncryptionHelper", "Decoded data length: ${decoded.size}")

            val ivSize = 12 // GCM recommended IV size
            require(decoded.size > ivSize) { "Invalid data format: Missing IV" }

            val iv = decoded.sliceArray(0 until ivSize)
            val encryptedData = decoded.sliceArray(ivSize until decoded.size)

            Log.d("EncryptionHelper", "Extracted IV (Base64): ${Base64.getEncoder().encodeToString(iv)}")
            Log.d("EncryptionHelper", "Extracted Encrypted Data (Base64): ${Base64.getEncoder().encodeToString(encryptedData)}")

            val cipher = Cipher.getInstance(transformation)
            val spec = GCMParameterSpec(tagSize, iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

            val decryptedData = cipher.doFinal(encryptedData)
            val decryptedString = String(decryptedData, Charsets.UTF_8)
            Log.d("EncryptionHelper", "Decrypted String: $decryptedString")

            return decryptedString
        } catch (e: Exception) {
            Log.e("EncryptionHelper", "Decryption failed: ${e.message}")
            throw e
        }
    }

    fun logKeyInfo() {
        try {
            val factory = SecretKeyFactory.getInstance(secretKey.algorithm, "AndroidKeyStore")
            val keyInfo = factory.getKeySpec(secretKey, KeyInfo::class.java) as KeyInfo

            Log.d("EncryptionHelper", "Key Algorithm: ${secretKey.algorithm}")
            Log.d("EncryptionHelper", "Inside Secure Hardware: ${keyInfo.isInsideSecureHardware}")
            Log.d("EncryptionHelper", "User Authentication Required: ${keyInfo.isUserAuthenticationRequired}")
            Log.d("EncryptionHelper", "Authentication Validity Duration: ${keyInfo.userAuthenticationValidityDurationSeconds}")
        } catch (e: Exception) {
            Log.e("EncryptionHelper", "KeyInfo logging failed: ${e.message}")
        }
    }

    companion object {
        fun testEncryptionHelper() {
            // Generate a fixed AES key for testing
            val testKey = SecretKeySpec("1234567890123456".toByteArray(), "AES")
            val encryptionHelper = EncryptionHelper(testKey)

            encryptionHelper.logKeyInfo()

            try {
                val testString = "Hello, World!"
                Log.d("EncryptionHelper", "Original Data: $testString")

                // Test encryption
                val encrypted = encryptionHelper.encrypt(testString)
                Log.d("EncryptionHelper", "Encrypted Data: $encrypted")

                // Test decryption
                val decrypted = encryptionHelper.decrypt(encrypted)
                Log.d("EncryptionHelper", "Decrypted Data: $decrypted")

                // Verify the output
                require(decrypted == testString) { "Decrypted data does not match the original!" }
                Log.d("EncryptionHelper", "Test passed: Encryption and decryption are consistent.")
            } catch (e: Exception) {
                Log.e("EncryptionHelper", "Test failed: ${e.message}")
            }
        }
    }
}
