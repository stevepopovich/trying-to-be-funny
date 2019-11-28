package com.stevenpopovich.trying_to_be_funny

import androidx.room.*
import java.util.*

private const val setTableName = "set_table"
private const val jokeTableName = "joke_table"
private const val placeTableName = "place_table"
private const val setJokeJoinTable = "set_joke_join"

@Entity(
    tableName = setTableName,
    foreignKeys = arrayOf(
        ForeignKey(
            entity = RoomPlace::class,
            parentColumns = arrayOf("placeId"),
            childColumns = arrayOf("placeId")
        )
    )
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
    fun getAll(): List<RoomSet>

    @Query("SELECT * FROM $setTableName WHERE id = :setId")
    fun get(setId: SetId): RoomSet

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
    fun getAll(): List<RoomJoke>

    @Query("SELECT * FROM $jokeTableName WHERE joke = :joke")
    fun get(joke: Joke): RoomJoke

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
    fun getAll():   List<RoomPlace>

    @Query("SELECT * FROM $placeTableName WHERE placeId = :placeId")
    fun get(placeId: String?): RoomPlace?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(place: RoomPlace)
}


@Entity(tableName = setJokeJoinTable,
    primaryKeys = arrayOf("joke","setId"),
    foreignKeys = arrayOf(
        ForeignKey(entity = RoomJoke::class,
            parentColumns = arrayOf("joke"),
            childColumns = arrayOf("joke")),
        ForeignKey(entity = RoomSet::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("setId"))
    )
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
    fun getJokesForSet(setId: SetId): List<RoomJoke>

    @Query("""
           SELECT * FROM $setTableName
           INNER JOIN $setJokeJoinTable
           ON $setTableName.id = $setJokeJoinTable.setId
           WHERE $setJokeJoinTable.joke=:joke
           """)
    fun getSetsForJoke(joke: Joke): List<RoomSet>
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

//    @TypeConverter
//    fun recordingPathToString(recordingPath: RecordingPath): String {
//        return recordingPath
//    }
//
//    @TypeConverter
//    fun stringToRecordingPath(path: String): RecordingPath {
//        return path
//    }
//
//    @TypeConverter
//    fun jokeToString(joke: Joke): String {
//        return joke
//    }
//
//    @TypeConverter
//    fun stringToJoke(jokeString: String): Joke {
//        return jokeString
//    }
}

@Database(entities = [RoomSet::class, RoomPlace::class, RoomJoke::class, JokeSetJoin::class], version = 1)
@TypeConverters(RoomSetConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun setDao(): SetDao
    abstract fun jokeDao(): JokeDao
    abstract fun placeDao(): PlaceDao
    abstract fun jokeSetJoin(): JokeSetJoinDao
}