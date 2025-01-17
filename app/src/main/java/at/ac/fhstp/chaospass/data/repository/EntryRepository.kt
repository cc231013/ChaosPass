package at.ac.fhstp.chaospass.data.repository

import at.ac.fhstp.chaospass.utils.EncryptionHelper
import at.ac.fhstp.chaospass.data.dao.EntryDao
import at.ac.fhstp.chaospass.data.entities.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class EntryRepository(
    private val entryDao: EntryDao,
    private val encryptionHelper: EncryptionHelper
) {

    suspend fun addEntry(siteName: String, username: String, password: String) {
        val encryptedSiteName = encryptionHelper.encrypt(siteName)
        val encryptedUsername = encryptionHelper.encrypt(username)
        val encryptedPassword = encryptionHelper.encrypt(password)

        val entry = Entry(
            siteName = encryptedSiteName,
            username = encryptedUsername,
            password = encryptedPassword
        )
        entryDao.addEntry(entry)
    }

    suspend fun getAllEntries(): List<Entry> {
        return entryDao.getAllEntries().map {
            Entry(
                id = it.id,
                siteName = encryptionHelper.decrypt(it.siteName),
                username = encryptionHelper.decrypt(it.username),
                password = encryptionHelper.decrypt(it.password)
            )
        }
    }

    suspend fun updateEntry(entry: Entry) {
        val encryptedEntry = Entry(
            id = entry.id,
            siteName = encryptionHelper.encrypt(entry.siteName),
            username = encryptionHelper.encrypt(entry.username),
            password = encryptionHelper.encrypt(entry.password)
        )
        entryDao.updateEntry(encryptedEntry)
    }

    suspend fun deleteEntry(entry: Entry) {
        entryDao.deleteEntry(entry)
    }

    suspend fun reEncryptAllEntries(newEncryptionHelper: EncryptionHelper) {
        withContext(Dispatchers.IO) {
            val allEntries = entryDao.getAllEntries()
            allEntries.forEach { entry ->
                val decryptedSiteName = encryptionHelper.decrypt(entry.siteName)
                val decryptedUsername = encryptionHelper.decrypt(entry.username)
                val decryptedPassword = encryptionHelper.decrypt(entry.password)

                val reEncryptedEntry = Entry(
                    id = entry.id,
                    siteName = newEncryptionHelper.encrypt(decryptedSiteName),
                    username = newEncryptionHelper.encrypt(decryptedUsername),
                    password = newEncryptionHelper.encrypt(decryptedPassword)
                )
                entryDao.updateEntry(reEncryptedEntry)
            }
        }
    }
}
