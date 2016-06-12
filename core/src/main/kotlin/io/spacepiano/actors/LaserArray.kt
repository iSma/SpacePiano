package io.spacepiano.actors

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool
import com.badlogic.gdx.scenes.scene2d.Group
import io.spacepiano.R
import io.spacepiano.midi.off
import io.spacepiano.midi.on
import io.spacepiano.midi.pitch
import javax.sound.midi.ShortMessage

class LaserArray : Group() {
    var level = 0
        get() = field
        set(value) {
            field = value
            if (field >= levels.size)
                field = levels.size - 1
            if (field < 0)
                field = 0
        }

    private val shots: List<List<ParticleActor>>

    private val levels = listOf("laser-1", "laser-2", "laser-3", "laser-4", "laser-5", "fire-1", "fire-2")
    private val pools = levels.map { ParticleEffectPool(R[R.EFFECTS[it]], 128, 128) }

    init {
        shots = pools.map { pool ->
            (0..127).map { pitch ->
                val effect = ParticleActor(pool.obtain())
                effect.setPosition((pitch - 60) * 32f, 0f)
                effect.start()
                effect.allowCompletion()
                addActor(effect)
                effect
            }
        }
    }

    fun shoot(message: ShortMessage) {
        if (message.on)
            shots[level][message.pitch].start()
        else if (message.off)
            shots.forEach { it[message.pitch].allowCompletion() }
    }
}