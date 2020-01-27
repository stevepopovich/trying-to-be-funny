package com.stevenpopovich

import com.stevenpopovich.trying_to_be_funny.PositiveFloat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

fun Date.isWithin(date: Date, timePlusMinusInSeconds: PositiveFloat): Boolean {
    return TimeUnit.MILLISECONDS.toSeconds(abs(this.time - date.time)) < timePlusMinusInSeconds.value;
}

fun Date.isPrettyMuchNow(): Boolean {
    return this.isWithin(Date(), PositiveFloat(3f))
}