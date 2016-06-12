package io.spacepiano

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import io.spacepiano.midi.Midi
import io.spacepiano.screens.LoadingScreen
import io.spacepiano.screens.Screen

object Game : Game() {
    val NAME = "SpacePiano"
    val WIDTH = 1024f
    val HEIGHT = 1024f

    override fun create() {
        Midi.synth.open()
        setScreen(LoadingScreen())
    }

    override fun setScreen(screen: com.badlogic.gdx.Screen?) {
        super.setScreen(screen)
        Gdx.app.log("setScreen", screen?.javaClass?.simpleName)
        Gdx.input.inputProcessor = when (screen) {
            is Screen -> screen.input
            else -> null
        }
    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        super.render()
    }

    override fun dispose() {
        Midi.synth.close()
    }
}
