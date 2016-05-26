package io.spacepiano.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import io.spacepiano.Game

object DesktopLauncher {
    @JvmStatic fun main(args: Array<String>) {
        createApplication()
    }

    private fun createApplication(): LwjglApplication {
        return LwjglApplication(Game, defaultConfiguration)
    }

    private val defaultConfiguration: LwjglApplicationConfiguration
        get() {
            val configuration = LwjglApplicationConfiguration()
            configuration.title = "SpacePiano"
            configuration.width = 768
            configuration.height = 768
            return configuration
        }
}