package io.spacepiano.screens

import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import io.spacepiano.Game
import io.spacepiano.R

class IntroScreen : Screen() {
    var done = false

    override val input = object : InputAdapter() {
        override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            done = true
            return true
        }

        override fun keyUp(keycode: Int): Boolean {
            done = true
            return true
        }
    }

    val title = Label(Game.NAME, R[R.SKIN], "title")
    init {
        val group = Group()
        stage.addActor(group)
        group.addActor(title)

        title.setOrigin(Align.center)
        title.setPosition(0f, 0f, Align.center)

        val action = Actions.sequence(
                Actions.scaleTo(96f, 96f),
                Actions.scaleTo(1f, 1f, 1.6f, Interpolation.exp10),
                Actions.delay(.2f),
                Actions.moveToAligned(0f, stage.height/2 - title.originY - 32f, Align.top, .8f, Interpolation.pow4In),
                Actions.delay(.8f),
                Actions.run { done = true }
        )

        group.addAction(action)
    }

    override fun render(delta: Float) {
        super.render(delta)

        if (done) {
            dispose()
            Game.screen = MainMenuScreen()
        }
    }
}