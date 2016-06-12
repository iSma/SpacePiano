package io.spacepiano.midi

import javax.sound.midi.MetaMessage
import javax.sound.midi.Sequence
import javax.sound.midi.ShortMessage
import javax.sound.midi.Track as JavaTrack

object Parser {
    fun parse(sequence: Sequence) = sequence.tracks.map { parseTrack(it, sequence.resolution) }

    private fun parseTrack(track: JavaTrack, resolution: Int): Track {
        val notes: MutableList<Note> = mutableListOf()
        val cache: MutableMap<Int, Pair<ShortMessage, Long>> = mutableMapOf()
        var tempo = 120
        var instrument = -1

        fun ticksToMs(ticks: Long) = (ticks.toDouble() / resolution / tempo * 60000).toLong()

        fun noteOff(message: ShortMessage, tick: Long) {
            cache[message.pitch]?.let {
                val (on, start) = it
                val duration = tick - start
                val note = Note(on.pitch, on.velocity, ticksToMs(duration).toInt(), ticksToMs(start))
                notes.add(note)
            }
        }

        fun noteOn(message: ShortMessage, tick: Long) =
                if (message.velocity == 0)
                    noteOff(message, tick)
                else
                    cache[message.pitch] = message to tick

        fun tempoChanged(message: MetaMessage) {
            tempo = 60000000 / message.data.foldIndexed(0) {
                i, result, value ->
                result or ((value.toInt() and 0xFF) shl (16 - 8 * i))
            }
        }

        for (i in 0..track.size() - 1) {
            val event = track[i]
            val message = event.message
            when (message) {
                is MetaMessage -> if (message.type == 0x51) tempoChanged(message)
                is ShortMessage -> when (message.command) {
                    ShortMessage.NOTE_ON -> noteOn(message, event.tick)
                    ShortMessage.NOTE_OFF -> noteOff(message, event.tick)
                    ShortMessage.PROGRAM_CHANGE -> if (instrument < 0) instrument = message.data1
                }
            }
        }

        if (instrument < 0)
            instrument = 0

        return Track(notes, instrument)
    }
}
