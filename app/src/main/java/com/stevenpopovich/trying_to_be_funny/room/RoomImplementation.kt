package com.stevenpopovich.trying_to_be_funny.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.stevenpopovich.trying_to_be_funny.Bit
import com.stevenpopovich.trying_to_be_funny.RecordingPath
import com.stevenpopovich.trying_to_be_funny.SetId
import java.util.*

private const val setTableName = "set_table"
private const val bitTableName = "bit_table"
private const val placeTableName = "place_table"
private const val setBitJoinTable = "set_bit_join"

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
class RoomBit(
     @PrimaryKey val bit: Bit
)

@Dao
interface BitDao {
    @Query("SELECT * FROM $bitTableName")
    fun getAll(): LiveData<List<RoomBit>>

    @Query("SELECT * FROM $bitTableName WHERE bit = :bit")
    fun get(bit: Bit): LiveData<RoomBit>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(bit: RoomBit)
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


@Entity(tableName = setBitJoinTable,
    primaryKeys = ["bit", "setId"],
    foreignKeys = [ForeignKey(entity = RoomBit::class,
        parentColumns = arrayOf("bit"),
        childColumns = arrayOf("bit")), ForeignKey(entity = RoomSet::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("setId"))
    ]
)
data class BitSetJoin(
    val bit: Bit,
    val setId: UUID
)

@Dao
interface BitSetJoinDao {
    @Insert
    fun insert(bitSetJoin: BitSetJoin)

    @Query("""
           SELECT * FROM $bitTableName
           INNER JOIN $setBitJoinTable
           ON $bitTableName.Bit = $setBitJoinTable.Bit
           WHERE $setBitJoinTable.setId=:setId
           """)
    fun getBitsForSet(setId: SetId): LiveData<List<RoomBit>>

    @Query("""
           SELECT * FROM $setTableName
           INNER JOIN $setBitJoinTable
           ON $setTableName.id = $setBitJoinTable.setId
           WHERE $setBitJoinTable.bit=:bit
           """)
    fun getSetsForBit(bit: Bit): LiveData<List<RoomSet>>
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

@Database(entities = [RoomSet::class, RoomPlace::class, RoomBit::class, BitSetJoin::class], version = 1)
@TypeConverters(RoomSetConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun setDao(): SetDao
    abstract fun bitDao(): BitDao
    abstract fun placeDao(): PlaceDao
    abstract fun bitSetJoin(): BitSetJoinDao
}