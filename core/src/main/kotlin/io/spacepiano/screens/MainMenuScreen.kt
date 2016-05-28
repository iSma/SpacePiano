package io.spacepiano.screens

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import io.spacepiano.Game
import io.spacepiano.menu.MainMenu
import io.spacepiano.menu.Menu
import io.spacepiano.menu.MenuScreen

class MainMenuScreen : MenuScreen() {
    init {
        val title = Menu(Game.NAME)
        val mainMenu = MainMenu(this)
        val fadeAction = Actions.sequence(Actions.fadeIn(.5f), Actions.removeActor(title))

        initMenu(title)
        stage.addActor(title)

        push(mainMenu, fadeAction) // TODO: Investigate why mainMenu doesn't fade in
    }
}
