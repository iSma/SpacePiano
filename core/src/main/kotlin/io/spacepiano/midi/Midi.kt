package io.spacepiano.midi

import javax.sound.midi.MidiDevice
import javax.sound.midi.MidiSystem
import javax.sound.midi.ShortMessage

object  Midi {
    val synth = MidiSystem.getSynthesizer()

    val devices: List<MidiDevice>
        get() = MidiSystem.getMidiDeviceInfo().map { MidiSystem.getMidiDevice(it) }.plus(VirtualDevice)

    val inputDevices: List<MidiDevice>
        get() = devices.filter { it.maxReceivers != 0 }

    val outputDevices: List<MidiDevice>
        get() = devices.filter { it.maxTransmitters != 0 }

    fun noteOn(pitch: Int, velocity: Int = 127, channel: Int = 0) = ShortMessage(0x90, channel, pitch, velocity)
    fun noteOff(pitch: Int, channel: Int = 0) = ShortMessage(0x80, channel, pitch, 0)
    fun programChange(program: Int, channel: Int = 0) = ShortMessage(0xC0, channel, program and  0x7F, program shr 7)
}

val ShortMessage.pitch: Int
    get() = data1

val ShortMessage.velocity: Int
    get() = data2

val ShortMessage.on: Boolean
    get() = command == 0x90

val ShortMessage.off: Boolean
    get() = command == 0x80
