package com.stevenpopovich.trying_to_be_funny

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
    fun getSet(recordingPath: RecordingPath): Set
    fun querySets(date: Date?, jokes: List<Joke>?, location: Place?)
    fun deleteSet(setId: SetId)
}

/**
 * This is class is responsible for managing Set data, but not the actual recording audio.
 * A "Set" contains the path to the actual audio, respective to the implementation type
 */
class SetServiceLocalSavingImpl: SetService {
    override fun saveStaticSet() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearStaticSet() {
        SetService.setDate = null
        SetService.setJokes = null
        SetService.setLocation = null
        SetService.setRecordingPath = null
    }

    override fun getSet(recordingPath: RecordingPath): Set {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun querySets(date: Date?, jokes: List<Joke>?, location: Place?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSet(setId: SetId) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}