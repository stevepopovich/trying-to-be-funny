package com.stevenpopovich.trying_to_be_funny

import java.util.*

typealias Bit = String

typealias Place = com.seatgeek.placesautocomplete.model.Place

typealias RecordingPath = String

typealias SetId = UUID

data class StandUpSet(
    val id: SetId,
    val bits: List<Bit>,
    val location: Place,
    val date: Date,
    val recordingPath: RecordingPath
)