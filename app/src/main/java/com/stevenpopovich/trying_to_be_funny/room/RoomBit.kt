package com.stevenpopovich.trying_to_be_funny.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.stevenpopovich.trying_to_be_funny.Bit

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
