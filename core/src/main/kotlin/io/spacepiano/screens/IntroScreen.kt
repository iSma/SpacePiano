package io.spacepiano.screens

import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import io.spacepiano.Game
import io.spacepiano.R
import io.spacepiano.actors.TransformActor

class IntroScreen : Screen() {
    var done = false

    val inputSkip = object : InputAdapter() {
        override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            done = true
            return true
        }

        override fun keyUp(keycode: Int): Boolean {
            done = true
            return true
        }
    }

    init {
        val label = Label(Game.NAME, R[R.SKIN], "title")
        val title = TransformActor(label)

        label.setOrigin(Align.center)
        label.setPosition(0f, 0f, Align.center)

        stage.addActor(title)

        val action = Actions.sequence(
                Actions.scaleTo(96f, 96f),
                Actions.scaleTo(1f, 1f, 1.6f, Interpolation.exp10),
                Actions.delay(.2f),
                Actions.moveToAligned(0f, stage.height / 2 - label.originY - 32f, Align.top, .8f, Interpolation.pow4In),
                Actions.delay(.8f),
                Actions.run { done = true }
        )

        title.addAction(action)
        input.addProcessor(inputSkip)
    }

    override fun render(delta: Float) {
        super.render(delta)

        if (done) {
            dispose()
            Game.screen = MainMenuScreen()
        }
    }
}