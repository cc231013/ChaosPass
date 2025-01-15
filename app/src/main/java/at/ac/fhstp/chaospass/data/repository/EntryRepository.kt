package at.ac.fhstp.chaospass.data.repository

import EncryptionHelper
import at.ac.fhstp.chaospass.data.dao.EntryDao
import at.ac.fhstp.chaospass.data.entities.Entry

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
}
