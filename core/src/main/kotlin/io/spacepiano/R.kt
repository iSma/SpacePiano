package io.spacepiano

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import javax.sound.midi.Sequence
import io.spacepiano.midi.Midi

// TODO: make cleaner using reified type parameters and/or lazy
object R : AssetManager(InternalFileHandleResolver()) {
    val skin = A("ui.json", Skin::class.java)

    val ships = listOf("blue", "green", "yellow", "white").map {
        it to A("ship-mod-$it.atlas", TextureAtlas::class.java)
    }.toMap()

    val effects = listOf("fire-1", "fire-2", "laser-1", "laser-2", "laser-3", "laser-4", "laser-5").map {
        it to A("particles/$it.p", ParticleEffect::class.java)
    }.toMap()

    val midi = listOf("ode-to-joy").map {
        it to A("assets/midi/$it.mid", Sequence::class.java)
    }.toMap()

    init {
        setLoader(Sequence::class.java, Midi.Loader(fileHandleResolver))

        loadNow(skin)
        ships.values.forEach { load(it) }
        effects.values.forEach { load(it) }
        midi.values.forEach { load(it) }
    }

    fun loadNow(asset: AssetDescriptor<*>) {
        load(asset)
        finishLoadingAsset(asset.fileName)
    }

    fun loadAll() = R::class.java.declaredMethods
            .filter { it.name.startsWith("get") and it.returnType.isAssignableFrom(AssetDescriptor::class.java) }
            .forEach { load(it(R) as AssetDescriptor<*>?) }

    private fun <T> A(fileName: String, type: Class<T>) = AssetDescriptor(fileName, type)
}