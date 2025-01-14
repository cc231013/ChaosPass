package at.ac.fhstp.chaospass.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "password") // Matches table name in EntryDao
data class Entry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val siteName: String,
    val username: String,
    val password: String
)
