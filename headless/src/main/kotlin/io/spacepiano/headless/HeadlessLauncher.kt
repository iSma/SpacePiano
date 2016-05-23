package io.spacepiano.headless

import com.badlogic.gdx.Application
import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration
import io.spacepiano.Game

/** Launches the headless application. Can be converted into a utilities project or a server application.  */
object HeadlessLauncher {
    @JvmStatic fun main(args: Array<String>) {
        createApplication()
    }

    private fun createApplication(): Application {
        // Note: you can use a custom ApplicationListener implementation for the headless project instead of Game.
        return HeadlessApplication(Game, defaultConfiguration)
    }

    private // When this value is negative, Game#render() is never called.
    val defaultConfiguration: HeadlessApplicationConfiguration
        get() {
            val configuration = HeadlessApplicationConfiguration()
            configuration.renderInterval = -1f
            return configuration
        }
}