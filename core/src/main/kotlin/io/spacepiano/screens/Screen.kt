package io.spacepiano.screens

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import io.spacepiano.Game

abstract class Screen : Screen {
    val stage = Stage()
    open val input: InputProcessor = stage

    init {
        stage.viewport = FitViewport(Game.WIDTH, Game.HEIGHT)
    }

    // Draw your screen here. "delta" is the time since last render in seconds.
    override fun render(delta: Float) {
        stage.act()
        stage.draw()
    }

    // Prepare your screen here.
    override fun show() {
    }

    // This method is called when another screen replaces this one.
    override fun hide() {
    }

    // Invoked when your application is paused.
    override fun pause() {
    }

    // Invoked when your application is resumed after pause.
    override fun resume() {
    }

    // Resize your screen here. The parameters represent the new window size.
    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height)
    }

    // Destroy screen's assets here.
    override fun dispose() {
        stage.dispose()
    }
}