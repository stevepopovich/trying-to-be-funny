package com.stevenpopovich.trying_to_be_funny.room

import androidx.lifecycle.LiveData
import androidx.room.*

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