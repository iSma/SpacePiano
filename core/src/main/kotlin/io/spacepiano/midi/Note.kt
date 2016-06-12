package io.spacepiano.midi

import javax.sound.midi.ShortMessage

data class Note(val pitch: Int, val velocity: Int, val duration: Int, val start: Long) {
    val octave = pitch / 12
    val tone = pitch % 12
    val black = tone in listOf(1, 3, 5, 8, 10)
    val white = !black

    val onMessage = ShortMessage(ShortMessage.NOTE_ON, pitch, velocity)
    val offMessage = ShortMessage(ShortMessage.NOTE_OFF, pitch, 0)
}