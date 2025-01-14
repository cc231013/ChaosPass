package at.ac.fhstp.chaospass.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import at.ac.fhstp.chaospass.data.entities.Entry

@Dao
interface EntryDao {
    @Insert
    suspend fun addEntry(entry: Entry): Long // Returns row ID of inserted entry

    @Query("SELECT * FROM password")
    suspend fun getAllEntries(): List<Entry>

    @Update
    suspend fun updateEntry(entry: Entry): Int // Returns number of rows updated

    @Delete
    suspend fun deleteEntry(entry: Entry): Int // Returns number of rows deleted
}
