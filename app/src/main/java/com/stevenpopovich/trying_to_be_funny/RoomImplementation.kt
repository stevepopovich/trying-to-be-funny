package com.stevenpopovich.trying_to_be_funny

import androidx.room.*
import java.util.*

@Entity(tableName = tableName)
class RoomSet(
    @PrimaryKey val id: SetId,
    @ColumnInfo(name = "jokes") val jokes: List<Joke>,
    @ColumnInfo(name = "location") val location: Place,
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "recording_path") val recordingPath: RecordingPath
) {
    fun toSet(): Set {
        return Set(
            id = id,
            date = date,
            jokes = jokes,
            location = location,
            recordingPath = recordingPath
        )
    }
}

private const val tableName = "set_table"

@Dao
interface SetDao {
    @Query("SELECT * FROM $tableName")
    fun getAll(): List<RoomSet>

    @Query("SELECT * FROM $tableName WHERE id = :setId")
    fun get(setId: SetId): RoomSet

    @Insert
    fun insert(set: RoomSet)

    @Query("DELETE FROM $tableName WHERE id = :setId")
    fun deleteById(setId: SetId)
}


@Database(entities = [RoomSet::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun setDao(): SetDao
}