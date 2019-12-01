package com.stevenpopovich.trying_to_be_funny

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

private const val setTableName = "set_table"
private const val jokeTableName = "joke_table"
private const val placeTableName = "place_table"
private const val setJokeJoinTable = "set_joke_join"

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

@Entity(tableName = jokeTableName)
class RoomJoke(
     @PrimaryKey val joke: Joke
)

@Dao
interface JokeDao {
    @Query("SELECT * FROM $jokeTableName")
    fun getAll(): LiveData<List<RoomJoke>>

    @Query("SELECT * FROM $jokeTableName WHERE joke = :joke")
    fun get(joke: Joke): LiveData<RoomJoke>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(joke: RoomJoke)
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


@Entity(tableName = setJokeJoinTable,
    primaryKeys = ["joke", "setId"],
    foreignKeys = [ForeignKey(entity = RoomJoke::class,
        parentColumns = arrayOf("joke"),
        childColumns = arrayOf("joke")), ForeignKey(entity = RoomSet::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("setId"))
    ]
)
data class JokeSetJoin(
    val joke: Joke,
    val setId: UUID
)

@Dao
interface JokeSetJoinDao {
    @Insert
    fun insert(jokeSetJoin: JokeSetJoin)

    @Query("""
           SELECT * FROM $jokeTableName
           INNER JOIN $setJokeJoinTable
           ON $jokeTableName.joke = $setJokeJoinTable.joke
           WHERE $setJokeJoinTable.setId=:setId
           """)
    fun getJokesForSet(setId: SetId): LiveData<List<RoomJoke>>

    @Query("""
           SELECT * FROM $setTableName
           INNER JOIN $setJokeJoinTable
           ON $setTableName.id = $setJokeJoinTable.setId
           WHERE $setJokeJoinTable.joke=:joke
           """)
    fun getSetsForJoke(joke: Joke): LiveData<List<RoomSet>>
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

@Database(entities = [RoomSet::class, RoomPlace::class, RoomJoke::class, JokeSetJoin::class], version = 1)
@TypeConverters(RoomSetConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun setDao(): SetDao
    abstract fun jokeDao(): JokeDao
    abstract fun placeDao(): PlaceDao
    abstract fun jokeSetJoin(): JokeSetJoinDao
}