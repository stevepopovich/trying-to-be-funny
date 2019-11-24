package com.stevenpopovich.trying_to_be_funny

import android.content.Context
import androidx.room.Room
import java.util.*

interface SetService {
    companion object {
        var setDate: Date? = null
        var setJokes: List<Joke>? = null
        var setLocation: Place? = null
        var setRecordingPath: RecordingPath? = null
    }

    fun saveStaticSet()
    fun clearStaticSet()
    fun getSet(setId: SetId): Set
    fun querySets(date: Date?, jokes: List<Joke>?, location: Place?)
    fun deleteSet(setId: SetId)
}

/**
 * This is class is responsible for managing Set data, but not the actual recording audio.
 * A "Set" contains the path to the actual audio, respective to the implementation type
 */
class SetServiceLocalSavingImpl(context: Context) : SetService {
    val database: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "room-trying-to-be-funny-database"
    ).build()

    override fun saveStaticSet() {
        validateStaticSet()

        database.setDao().insert(
            RoomSet(
                SetId.randomUUID(),
                SetService.setJokes!!,
                SetService.setLocation!!,
                SetService.setDate!!,
                SetService.setRecordingPath!!
            )
        )

        clearStaticSet()
    }

    override fun clearStaticSet() {
        SetService.setDate = null
        SetService.setJokes = null
        SetService.setLocation = null
        SetService.setRecordingPath = null
    }

    override fun getSet(setId: SetId): Set {
        return database.setDao().get(setId).toSet()
    }

    override fun querySets(date: Date?, jokes: List<Joke>?, location: Place?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSet(setId: SetId) {
        database.setDao().deleteById(setId)
    }

    private fun validateStaticSet() {
        if (SetService.setDate == null)
            throw SetDataInvalidError(property = "SetDate", value = SetService.setDate)

        if (SetService.setJokes == null)
            throw SetDataInvalidError(property = "SetJokes", value = SetService.setJokes)

        if (SetService.setLocation == null)
            throw SetDataInvalidError(property = "SetLocation", value = SetService.setLocation)

        if (SetService.setRecordingPath == null)
            throw SetDataInvalidError(property = "SetRecordingPath", value = SetService.setRecordingPath)
    }
}

data class SetDataInvalidError(val property: String, val value: Any?): Error("$property was null or invalid! Value: $value")