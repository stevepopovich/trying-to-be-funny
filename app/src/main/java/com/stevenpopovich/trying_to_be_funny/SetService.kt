package com.stevenpopovich.trying_to_be_funny

import android.arch.convert.toObservable
import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.room.Room
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import java.util.*

interface SetService {
    companion object {
        var setJokes: List<Joke>? = null
        var setLocation: Place? = null
        var setRecordingPath: RecordingPath? = null
    }

    fun saveStaticSet()
    fun clearStaticSet()
    fun getSet(setId: SetId): Observable<StandUpSet>
    fun querySets(date: Date?, jokes: List<Joke>?, location: Place?): List<StandUpSet>
    fun deleteSet(setId: SetId)
}

/**
 * This is class is responsible for managing StandUpSet data, but not the actual recording audio.
 * A "StandUpSet" contains the path to the actual audio, respective to the implementation type
 */
class SetServiceLocalSavingImpl(context: Context) : SetService {
    private val database: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "room-trying-to-be-funny-database"
    ).build()

    override fun saveStaticSet() {
        validateStaticSet()

        val setId = SetId.randomUUID()

        AsyncTask.execute {
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
    }

    override fun clearStaticSet() {
        SetService.setJokes = null
        SetService.setLocation = null
        SetService.setRecordingPath = null
    }

    override fun getSet(setId: SetId): Observable<StandUpSet> {
        return database.setDao().get(setId).toObservable().flatMap { roomSet ->
            val placeObservable = database.placeDao().get(roomSet.placeId).toObservable()
            val jokesObservable = database.jokeSetJoin().getJokesForSet(roomSet.id).toObservable()
            Observables.combineLatest(
                placeObservable,
                jokesObservable
            ) { roomPlace: RoomPlace?, jokes: List<RoomJoke>  ->
                val place = Place(
                    roomPlace?.description,
                    roomPlace?.placeId,
                    listOf(),
                    listOf(),
                    listOf()
                )

                StandUpSet(
                    id = roomSet.id,
                    recordingPath = roomSet.recordingPath,
                    jokes = jokes.map { it.joke },
                    date = roomSet.date,
                    location = place
                )
            }
        }
    }

    override fun querySets(date: Date?, jokes: List<Joke>?, location: Place?): List<StandUpSet> {
        throw NotImplementedError()
    }

    override fun deleteSet(setId: SetId) {
        database.setDao().deleteById(setId)
    }

    private fun validateStaticSet() {
        if (SetService.setJokes == null)
            throw SetDataInvalidError(property = "SetJokes", value = SetService.setJokes)

        if (SetService.setLocation == null)
            throw SetDataInvalidError(property = "SetLocation", value = SetService.setLocation)

        if (SetService.setRecordingPath == null)
            throw SetDataInvalidError(property = "SetRecordingPath", value = SetService.setRecordingPath)
    }

    fun getAllSets(): LiveData<List<RoomSet>> {
        return database.setDao().getAll()
    }
}

data class SetDataInvalidError(val property: String, val value: Any?): Error("$property was null or invalid! Value: $value")