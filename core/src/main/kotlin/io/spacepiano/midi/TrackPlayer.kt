package io.spacepiano.midi

import javax.sound.midi.MidiMessage

class TrackPlayer(val track: Track) {
    private var lastUpdate = 0L
    private var timeFrame = 0L..0L

    val notes: List<Note>
        get() = timeFrame.flatMap { track.byTime[it].orEmpty() }

    val messages: List<MidiMessage>
        get() = timeFrame.flatMap { track.messages[it].orEmpty() }

    fun update(delta: Float) {
        val timeMs = lastUpdate + (delta * 1000).toLong()
        timeFrame = lastUpdate until timeMs
        lastUpdate = timeMs
    }

    fun seek(time: Float) {
        val timeMs = lastUpdate + (time * 1000).toLong()
        timeFrame = timeMs..timeMs
        lastUpdate = timeMs
    }
}