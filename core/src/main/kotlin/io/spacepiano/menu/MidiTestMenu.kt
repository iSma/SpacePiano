package io.spacepiano.menu

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import io.spacepiano.actors.LaserArray
import io.spacepiano.midi.Midi
import io.spacepiano.midi.VirtualDevice
import javax.sound.midi.MidiMessage
import javax.sound.midi.Receiver
import javax.sound.midi.ShortMessage

class MidiTestMenu(screen: MenuScreen) : Menu("MIDI", menuStyle = "menu-small"), Receiver {
    val laserArray = LaserArray()

    val back = {
        screen.pop()
        laserArray.remove()
        Midi.outputDevices.forEach { it.close() }
        screen.input.removeProcessor(VirtualDevice.input)
        screen.input.removeProcessor(inputChangeLaser)
    }

    val inputChangeLaser = object : InputAdapter() {
        override fun keyDown(keycode: Int): Boolean {
            if (keycode == Input.Keys.LEFT)
                laserArray.level--
            else if (keycode == Input.Keys.RIGHT)
                laserArray.level++
            else
                return false

            return true
        }
    }

    init {
        val synthRcv = Midi.synth.receiver
        Midi.outputDevices.forEach {
            it.close()
            println(it.deviceInfo.name)
            it.open()
            it.transmitter.receiver = this
            it.transmitter.receiver = synthRcv
        }

        val items = Midi.synth.availableInstruments
                .mapIndexed { i, instrument -> instrument.name to { Midi.synth.channels[0].programChange(i) } }
                .plus("Back" to back)

        setItems(items)

        laserArray.setPosition(0f, -screen.stage.height/2)
        screen.stage.addActor(laserArray)

        screen.input.addProcessor(VirtualDevice.input)
        screen.input.addProcessor(inputChangeLaser)
    }

    override fun send(message: MidiMessage?, timeStamp: Long) {
        when (message) {
            is ShortMessage -> laserArray.shoot(message)
        }
    }

    override fun close() {
    }
}