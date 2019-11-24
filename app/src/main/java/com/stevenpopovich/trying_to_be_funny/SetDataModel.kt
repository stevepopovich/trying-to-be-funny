package com.stevenpopovich.trying_to_be_funny

import java.util.*

typealias Joke = String

typealias Place = com.seatgeek.placesautocomplete.model.Place

typealias RecordingPath = String

data class Set(
    val jokes: List<Joke>,
    val location: Place,
    val date: Date,
    val recordingPath: RecordingPath
)