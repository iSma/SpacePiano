package io.spacepiano

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import io.spacepiano.screens.LoadingScreen
import io.spacepiano.screens.Screen

object  Game : Game() {
    override fun create() {
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
}
