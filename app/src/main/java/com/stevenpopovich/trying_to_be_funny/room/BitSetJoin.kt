package com.stevenpopovich.trying_to_be_funny.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.stevenpopovich.trying_to_be_funny.Bit
import com.stevenpopovich.trying_to_be_funny.SetId
import java.util.*

@Entity(tableName = bitSetJoinTable,
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
           INNER JOIN $bitSetJoinTable
           ON $bitTableName.Bit = $bitSetJoinTable.Bit
           WHERE $bitSetJoinTable.setId=:setId
           """)
    fun getBitsForSet(setId: SetId): LiveData<List<RoomBit>>

    @Query("""
           SELECT * FROM $setTableName
           INNER JOIN $bitSetJoinTable
           ON $setTableName.id = $bitSetJoinTable.setId
           WHERE $bitSetJoinTable.bit=:bit
           """)
    fun getSetsForBit(bit: Bit): LiveData<List<RoomSet>>
}