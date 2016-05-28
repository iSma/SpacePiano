package io.spacepiano.midi

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import io.spacepiano.Game
import javax.sound.midi.MidiDevice
import javax.sound.midi.MidiDevice.Info
import javax.sound.midi.MidiMessage
import javax.sound.midi.Receiver
import javax.sound.midi.Transmitter

object VirtualDevice : MidiDevice {
    private val transmitters = mutableListOf<VirtualTransmitter>()
    private val info = object : Info("VirtualDevice", Game.NAME, "", "1.0.0") {}

    val keyMap = listOf(
            Input.Keys.Q,
            Input.Keys.NUM_2,
            Input.Keys.W,
            Input.Keys.NUM_3,
            Input.Keys.E,
            Input.Keys.R,
            Input.Keys.NUM_5,
            Input.Keys.T,
            Input.Keys.NUM_6,
            Input.Keys.Y,
            Input.Keys.NUM_7,
            Input.Keys.U,
            Input.Keys.I,
            Input.Keys.NUM_9,
            Input.Keys.O,
            Input.Keys.NUM_0,
            Input.Keys.P
    ).mapIndexed { i, key -> key to 60 + i }.associate { it }

    val input = object : InputAdapter() {
        var program = 200
        override fun keyDown(keycode: Int): Boolean {
            if (!keyMap.containsKey(keycode))
                return false

            send(Midi.noteOn(keyMap.getOrElse(keycode, { 0 })))
            return true
        }

        override fun keyUp(keycode: Int): Boolean {
            if (!keyMap.containsKey(keycode))
                return false

            send(Midi.noteOff(keyMap.getOrElse(keycode, { 0 })))
            return true
        }
    }

    fun send(message: MidiMessage) {
        transmitters.forEach { it.receiver?.send(message, -1) }
    }

    override fun getDeviceInfo(): Info? = info

    override fun isOpen() = true
    override fun open() {
    }

    override fun close() {
    }

    override fun getMaxTransmitters() = -1
    override fun getTransmitters() = transmitters
    override fun getTransmitter(): Transmitter? {
        val transmitter = VirtualTransmitter()
        transmitters.add(transmitter)
        return transmitter
    }


    override fun getMaxReceivers() = 0
    override fun getReceivers() = listOf<Receiver>()
    override fun getReceiver() = null

    override fun getMicrosecondPosition() = -1L

    class VirtualTransmitter : Transmitter {
        private var receiver: Receiver? = null
        override fun getReceiver() = receiver

        override fun setReceiver(receiver: Receiver?) {
            this.receiver = receiver
        }

        override fun close() {
        }
    }
}
