package com.stevenpopovich.trying_to_be_funny

import java.util.*

interface SetService {
    companion object {
        var setDate: Date? = null
        var setJokes: List<Joke>? = null
        var setLocation: Place? = null
        var setRecordingPath: RecordingPath? = null
    }

    fun saveSet()
    fun clearSet()
    fun getSet(recordingPath: RecordingPath): Set
    fun querySets(date: Date?, jokes: List<Joke>?, location: Place?)

}

class SetServiceImpl: SetService {
    override fun saveSet() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearSet() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSet(recordingPath: RecordingPath): Set {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun querySets(date: Date?, jokes: List<Joke>?, location: Place?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}