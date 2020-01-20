package com.stevenpopovich.trying_to_be_funny

import java.util.*

typealias Bit = String

typealias RecordingId = String

typealias SetId = UUID

data class StandUpSet(
    val id: SetId,
    val bits: List<Bit>,
    val location: Place,
    val date: Date,
    val recordingId: RecordingId
)

data class Place(
    val id: String,
    val name: String
)

fun com.google.android.libraries.places.api.model.Place.toPlace(): Place? {
    if (this.name != null && this.id != null) {
        return Place(
            this.id!!,
            this.name!!
        )
    }

    return null
}