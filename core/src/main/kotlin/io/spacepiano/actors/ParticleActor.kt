package io.spacepiano.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.ParticleEffect

class ParticleActor(val effect: ParticleEffect) : Transformable() {
    override fun drawTransformed(batch: Batch?, parentAlpha: Float) = effect.draw(batch)

    override fun act(delta: Float) {
        super.act(delta)
        effect.update(delta)
    }

    fun start() = effect.start()
}
