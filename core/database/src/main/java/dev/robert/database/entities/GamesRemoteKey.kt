package dev.robert.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val next: Int? = null,
    val previous: Int? = null,
    val lastUpdated: Long
)
