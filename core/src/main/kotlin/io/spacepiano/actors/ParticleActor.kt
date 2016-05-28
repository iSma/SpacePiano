package io.spacepiano.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.scenes.scene2d.Actor

class ParticleActor(val effect: ParticleEffect) : TransformActor(Wrapper(effect)) {
    fun start() = effect.start()

    private class Wrapper(val effect: ParticleEffect) : Actor() {
        override fun draw(batch: Batch?, parentAlpha: Float) = effect.draw(batch)
        override fun act(delta: Float) {
            super.act(delta)
            effect.update(delta)
        }
    }
}
