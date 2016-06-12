package io.spacepiano.midi

import javax.sound.midi.ShortMessage

class Track(val notes: List<Note>, val instrument: Int = 0) {
    val byTime: Map<Long, List<Note>>
    val messages: Map<Long, List<ShortMessage>>

    init {
        val byTime = mutableMapOf<Long, MutableList<Note>>()
        val messages = mutableMapOf<Long, MutableList<ShortMessage>>()

        notes.forEach {
            byTime.getOrPut(it.start, { mutableListOf() }).add(it)

            messages.getOrPut(it.start, { mutableListOf() }).add(it.onMessage)
            messages.getOrPut(it.start + it.duration, { mutableListOf() }).add(it.offMessage)
        }

        this.byTime = byTime
        this.messages = messages
    }
}
