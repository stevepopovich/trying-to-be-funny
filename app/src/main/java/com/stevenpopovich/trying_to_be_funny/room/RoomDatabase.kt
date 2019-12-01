package com.stevenpopovich.trying_to_be_funny.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.*

const val placeTableName = "place_table"
const val bitTableName = "bit_table"
const val setTableName = "set_table"
const val bitSetJoinTable = "set_bit_join"

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