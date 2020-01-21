package com.stevenpopovich.trying_to_be_funny

import android.arch.convert.toObservable
import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.stevenpopovich.trying_to_be_funny.SetService.Companion.clearStaticSet
import com.stevenpopovich.trying_to_be_funny.room.*
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import java.util.*

/**
 * This is class is responsible for managing StandUpSet data, but not the actual recording audio.
 * A "StandUpSet" is just the metadata about the content of the audio
 * A "StandUpSet" contains the path to the actual audio
 */
interface SetService {
    companion object {
        var setBits: List<Bit>? = null
        var setLocation: Place? = null
        var setRecordingId: RecordingId? = null
        fun clearStaticSet() {
            setBits = null
            setLocation = null
            setRecordingId = null
        }
    }

    fun saveStaticSet()
    fun getSet(setId: SetId): Observable<StandUpSet>
    fun querySets(date: Date?, bits: List<Bit>?, location: Place?): List<StandUpSet>
    fun deleteSet(setId: SetId)
    fun getAllBits(): LiveData<List<RoomBit>>
}

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
                    SetService.setLocation!!.id,
                    SetService.setLocation!!.name
                )
            )

            database.setDao().insert(
                RoomSet(
                    setId,
                    SetService.setLocation!!.id,
                    Date(),
                    SetService.setRecordingId!!
                )
            )

            SetService.setBits!!.forEach {
                database.bitDao().insert(
                    RoomBit(
                        it
                    )
                )

                database.bitSetJoin().insert(
                    BitSetJoin(
                        it,
                        setId
                    )
                )
            }

            clearStaticSet()
        }
    }

    override fun getSet(setId: SetId): Observable<StandUpSet> {
        return database.setDao().get(setId).toObservable().flatMap { roomSet ->
            val placeObservable = database.placeDao().get(roomSet.placeId).toObservable()
            val bitsObservable = database.bitSetJoin().getBitsForSet(roomSet.id).toObservable()
            Observables.combineLatest(
                placeObservable,
                bitsObservable
            ) { roomPlace: RoomPlace?, bits: List<RoomBit>  ->
                val place = Place(
                    roomPlace!!.placeId,
                    roomPlace.name
                )

                StandUpSet(
                    id = roomSet.id,
                    recordingId = roomSet.recordingId,
                    bits = bits.map { it.bit },
                    date = roomSet.date,
                    location = place
                )
            }
        }
    }

    override fun querySets(date: Date?, bits: List<Bit>?, location: Place?): List<StandUpSet> {
        throw NotImplementedError()
    }

    override fun deleteSet(setId: SetId) = database.setDao().deleteById(setId)

    override fun getAllBits(): LiveData<List<RoomBit>> = database.bitDao().getAll()

    private fun validateStaticSet() {
        if (SetService.setBits == null)
            throw SetDataInvalidError(property = "SetBits", value = SetService.setBits)

        if (SetService.setLocation == null)
            throw SetDataInvalidError(property = "SetLocation", value = SetService.setLocation)

        if (SetService.setRecordingId == null)
            throw SetDataInvalidError(property = "SetRecordingPath", value = SetService.setRecordingId)
    }

    fun getAllSets(): LiveData<List<RoomSet>> = database.setDao().getAll()
}

data class SetDataInvalidError(val property: String, val value: Any?): Error("$property was null or invalid! Value: $value")