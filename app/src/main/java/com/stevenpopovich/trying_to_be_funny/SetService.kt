package com.stevenpopovich.trying_to_be_funny

import android.content.Context
import androidx.room.Room
import java.util.*

interface SetService {
    companion object {
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

        val setId = SetId.randomUUID()

        database.placeDao().insert(
            RoomPlace(
                SetService.setLocation!!.place_id,
                SetService.setLocation!!.description
            )
        )

        database.setDao().insert(
            RoomSet(
                setId,
                SetService.setLocation!!.place_id,
                Date(),
                SetService.setRecordingPath!!
            )
        )

        SetService.setJokes!!.forEach {
            database.jokeDao().insert(
                RoomJoke(
                    it
                )
            )

            database.jokeSetJoin().insert(
                JokeSetJoin(
                    it,
                    setId
                )
            )
        }

        clearStaticSet()
    }

    override fun clearStaticSet() {
        SetService.setJokes = null
        SetService.setLocation = null
        SetService.setRecordingPath = null
    }

    override fun getSet(setId: SetId): Set {
        val roomSet = database.setDao().get(setId)

        val jokes = database.jokeSetJoin().getJokesForSet(roomSet.id)

        val roomPlace = database.placeDao().get(roomSet.placeId)

        val place = Place(
            roomPlace?.description,
            roomPlace?.placeId,
            listOf(),
            listOf(),
            listOf()
        )

        return Set(
            id = roomSet.id,
            recordingPath = roomSet.recordingPath,
            jokes = jokes.map { it.joke },
            date = roomSet.date,
            location = place
        )
    }

    override fun querySets(date: Date?, jokes: List<Joke>?, location: Place?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSet(setId: SetId) {
        database.setDao().deleteById(setId)
    }

    fun getRandomSet(): Set {
        return getSet(database.setDao().getAll().random().id)
    }

    private fun validateStaticSet() {
        if (SetService.setJokes == null)
            throw SetDataInvalidError(property = "SetJokes", value = SetService.setJokes)

        if (SetService.setRecordingPath == null)
            throw SetDataInvalidError(property = "SetRecordingPath", value = SetService.setRecordingPath)
    }
}

data class SetDataInvalidError(val property: String, val value: Any?): Error("$property was null or invalid! Value: $value")