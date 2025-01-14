package at.ac.fhstp.chaospass.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.sqlcipher.database.SupportFactory
import at.ac.fhstp.chaospass.data.dao.EntryDao
import at.ac.fhstp.chaospass.data.entities.Entry

@Database(entities = [Entry::class], version = 1, exportSchema = false)
abstract class EntryDatabase : RoomDatabase() {
    abstract fun entryDao(): EntryDao

    companion object {
        @Volatile
        private var INSTANCE: EntryDatabase? = null

        fun getDatabase(context: Context, passphrase: String): EntryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EntryDatabase::class.java,
                    "encrypted_entries"
                )
                    .openHelperFactory(SupportFactory(passphrase.toByteArray())) // SQLCipher support
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

