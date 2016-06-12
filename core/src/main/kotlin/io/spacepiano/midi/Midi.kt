package io.spacepiano.midi

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import javax.sound.midi.MidiDevice
import javax.sound.midi.MidiSystem
import javax.sound.midi.Sequence
import javax.sound.midi.ShortMessage

object Midi {
    val parser = Parser

    val synth = MidiSystem.getSynthesizer()

    val devices: List<MidiDevice>
        get() = MidiSystem.getMidiDeviceInfo().map { MidiSystem.getMidiDevice(it) }.plus(VirtualDevice)

    val inputDevices: List<MidiDevice>
        get() = devices.filter { it.maxReceivers != 0 }

    val outputDevices: List<MidiDevice>
        get() = devices.filter { it.maxTransmitters != 0 }

    fun noteOn(pitch: Int, velocity: Int = 127, channel: Int = 0) = ShortMessage(ShortMessage.NOTE_ON, channel, pitch, velocity)
    fun noteOff(pitch: Int, channel: Int = 0) = ShortMessage(ShortMessage.NOTE_OFF, channel, pitch, 0)

	class Loader(resolver: FileHandleResolver) : AsynchronousAssetLoader<Sequence, Midi.Loader.Parameters>(resolver) {
		var sequence: Sequence? = null

		override fun loadAsync(manager: AssetManager?, fileName: String?, file: FileHandle?, parameter: Parameters?) {
			sequence = null
            println(fileName)
            println(file?.file()?.absolutePath)
			sequence = MidiSystem.getSequence(file?.file())
		}

		override fun loadSync(manager: AssetManager?, fileName: String?, file: FileHandle?, parameter: Parameters?): Sequence? {
			val sequence = this.sequence
			this.sequence = null
			return sequence
		}

		override fun getDependencies(fileName: String?, file: FileHandle?, parameter: Parameters?) = null
		class Parameters : AssetLoaderParameters<Sequence>()
	}
}

val ShortMessage.pitch: Int
    get() = data1

val ShortMessage.velocity: Int
    get() = data2

val ShortMessage.on: Boolean
    get() = command == ShortMessage.NOTE_ON

val ShortMessage.off: Boolean
    get() = command == ShortMessage.NOTE_OFF
