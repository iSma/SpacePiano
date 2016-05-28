package io.spacepiano.actors

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group

open class TransformActor(val actor: Actor) : Group() {
    init {
        addActor(actor)
    }
}
