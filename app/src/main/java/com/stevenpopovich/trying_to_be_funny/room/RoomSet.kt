package com.stevenpopovich.trying_to_be_funny.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.stevenpopovich.trying_to_be_funny.RecordingPath
import com.stevenpopovich.trying_to_be_funny.SetId
import java.util.*

@Entity(
    tableName = setTableName,
    foreignKeys = [
        ForeignKey(
            entity = RoomPlace::class,
            parentColumns = arrayOf("placeId"),
            childColumns = arrayOf("placeId")
        )
    ]
)
data class RoomSet(
    @PrimaryKey val id: SetId,
    @ColumnInfo(name = "placeId") val placeId: String?,
    val date: Date,
    val recordingPath: RecordingPath
)

@Dao
interface SetDao {
    @Query("SELECT * FROM $setTableName")
    fun getAll(): LiveData<List<RoomSet>>

    @Query("SELECT * FROM $setTableName WHERE id = :setId")
    fun get(setId: SetId): LiveData<RoomSet>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(set: RoomSet)

    @Query("DELETE FROM $setTableName WHERE id = :setId")
    fun deleteById(setId: SetId)
}