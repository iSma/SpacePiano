package io.spacepiano.menu

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import io.spacepiano.Game
import io.spacepiano.R
import io.spacepiano.midi.*
import javax.sound.midi.MidiMessage
import javax.sound.midi.Receiver
import javax.sound.midi.ShortMessage

class MidiTestMenu(screen: MenuScreen) : Menu("MIDI", menuStyle = "menu-small"), Receiver {
    val back = {
        screen.pop()
        Midi.outputDevices.forEach { it.close() }
        screen.input.removeProcessor(VirtualDevice.input)
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
        screen.input.addProcessor(VirtualDevice.input)
    }

    override fun send(message: MidiMessage?, timeStamp: Long) {
        when (message) {
            is ShortMessage -> if (message.on) {
                val label = Label(message.pitch.toString(), R[R.SKIN], "title")
                val action = Actions.sequence(
                        Actions.moveBy(0f, Game.HEIGHT, .5f, Interpolation.pow4In),
                        Actions.removeActor()
                )

                label.color.a = .2f + .8f * message.velocity / 127f
                label.setPosition(-Game.WIDTH / 2, -Game.HEIGHT / 2)
                label.addAction(action)
                stage.addActor(label)
            }
        }
    }

    override fun close() {
    }
}