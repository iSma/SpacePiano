package io.spacepiano.menu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import io.spacepiano.Game

class MainMenu(screen: MenuScreen) : Menu(Game.NAME) {
    init {
        val start = {
            val action = Actions.sequence(
                    Actions.delay(.2f),
                    Actions.fadeOut(.2f),
                    Actions.delay(.4f)
            )
            screen.pop(action)
        }

        val exit = {
            val action = Actions.sequence(
                    Actions.fadeOut(.2f),
                    Actions.delay(.4f),
                    Actions.run { Gdx.app.exit() }
            )
            screen.pop(action)
        }

        setItems(listOf(
                "Start" to start,
                "Settings" to { screen.push(SettingsMenu(screen)) },
                "Exit" to exit
        ))
    }
}