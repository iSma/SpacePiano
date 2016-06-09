package io.spacepiano.actors

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Group

abstract class Transformable : Group() {
    abstract fun drawTransformed(batch: Batch?, parentAlpha: Float)

    override fun drawChildren(batch: Batch?, parentAlpha: Float) = drawTransformed(batch, parentAlpha)
}
