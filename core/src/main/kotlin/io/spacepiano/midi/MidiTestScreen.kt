package io.spacepiano.midi

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import io.spacepiano.Game
import io.spacepiano.R
import io.spacepiano.menu.Menu
import io.spacepiano.menu.MenuScreen
import io.spacepiano.screens.MainMenuScreen
import javax.sound.midi.*

class MidiTestScreen : MenuScreen(), Receiver {
    init {
        Midi.synth.open()
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
                .plus("Back" to { Game.screen = MainMenuScreen() })

        val menu = Menu("MIDI", items, menuStyle = "menu-small")
        push(menu)
        input.addProcessor(VirtualDevice.input)
    }

    override fun dispose() {
        super.dispose()
        Midi.synth.close()
    }

    override fun send(message: MidiMessage?, timeStamp: Long) {
        when (message) {
            is ShortMessage -> if (message.command == 0x90) {
                val label = Label(message.data1.toString(), R[R.SKIN], "title")
                val action = Actions.sequence(
                        Actions.moveBy(0f, Game.HEIGHT, .5f, Interpolation.pow4In),
                        Actions.removeActor()
                )

                label.color.a = .2f + .8f*message.data2/127f
                label.setPosition(-Game.WIDTH/2, -Game.HEIGHT/2)
                label.addAction(action)
                stage.addActor(label)
            }
        }
    }

    override fun close() { }
}