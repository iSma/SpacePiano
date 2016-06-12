package io.spacepiano.screens

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import io.spacepiano.Game
import io.spacepiano.R

class LoadingScreen : Screen() {
    val spinner = Label("", R[R.skin], "title")
    val animation =
            "[:....] [::...] [:::..] [::::.] [:::::] [.::::] [..:::] [...::] [....:] [.....]".split(" ")

    var i = 0
    val spinAction = Actions.forever(Actions.sequence(
            Actions.run { i = (i + 1) % animation.size; spinner.setText(animation[i]) },
            Actions.delay(.2f)
    ))

    val fadeOutAction = Actions.sequence(
            Actions.delay(.5f),
            Actions.fadeOut(.5f, Interpolation.fade),
            Actions.run { Game.screen = IntroScreen(); dispose() }
    )

    init {
        R.loadAll()

        stage.addActor(spinner)
        spinner.addAction(spinAction)
        spinner.setAlignment(Align.center)
        spinner.setPosition(0f, 0f, Align.center)
    }

    override fun render(delta: Float) {
        super.render(delta)
        if (R.update() && spinner.actions.size < 2)
            spinner.addAction(fadeOutAction)
    }
}