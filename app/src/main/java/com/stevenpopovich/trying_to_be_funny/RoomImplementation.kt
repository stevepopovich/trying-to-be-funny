package com.stevenpopovich.trying_to_be_funny

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

private const val setTableName = "set_table"
private const val bitTableName = "bit_table"
private const val placeTableName = "place_table"
private const val setbitJoinTable = "set_bit_join"

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

@Entity(tableName = bitTableName)
class Roombit(
     @PrimaryKey val bit: bit
)

@Dao
interface bitDao {
    @Query("SELECT * FROM $bitTableName")
    fun getAll(): LiveData<List<Roombit>>

    @Query("SELECT * FROM $bitTableName WHERE bit = :bit")
    fun get(bit: bit): LiveData<Roombit>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(bit: Roombit)
}

@Entity(tableName = placeTableName)
class RoomPlace(
    @PrimaryKey val placeId: String,
    val description: String
)
@Dao
interface PlaceDao {
    @Query("SELECT * FROM $placeTableName")
    fun getAll(): LiveData<List<RoomPlace>>

    @Query("SELECT * FROM $placeTableName WHERE placeId = :placeId")
    fun get(placeId: String?): LiveData<RoomPlace>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(place: RoomPlace)
}


@Entity(tableName = setbitJoinTable,
    primaryKeys = ["bit", "setId"],
    foreignKeys = [ForeignKey(entity = Roombit::class,
        parentColumns = arrayOf("bit"),
        childColumns = arrayOf("bit")), ForeignKey(entity = RoomSet::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("setId"))
    ]
)
data class bitSetJoin(
    val bit: bit,
    val setId: UUID
)

@Dao
interface bitSetJoinDao {
    @Insert
    fun insert(bitSetJoin: bitSetJoin)

    @Query("""
           SELECT * FROM $bitTableName
           INNER JOIN $setbitJoinTable
           ON $bitTableName.bit = $setbitJoinTable.bit
           WHERE $setbitJoinTable.setId=:setId
           """)
    fun getbitsForSet(setId: SetId): LiveData<List<Roombit>>

    @Query("""
           SELECT * FROM $setTableName
           INNER JOIN $setbitJoinTable
           ON $setTableName.id = $setbitJoinTable.setId
           WHERE $setbitJoinTable.bit=:bit
           """)
    fun getSetsForbit(bit: bit): LiveData<List<RoomSet>>
}

private class RoomSetConverters {
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun uuidToString(uuid: UUID): String {
        return uuid.toString()
    }

    @TypeConverter
    fun stringToUUID(id: String): UUID {
        return UUID.fromString(id)
    }
}

@Database(entities = [RoomSet::class, RoomPlace::class, Roombit::class, bitSetJoin::class], version = 1)
@TypeConverters(RoomSetConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun setDao(): SetDao
    abstract fun bitDao(): bitDao
    abstract fun placeDao(): PlaceDao
    abstract fun bitSetJoin(): bitSetJoinDao
}