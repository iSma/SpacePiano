package io.spacepiano

import com.badlogic.gdx.Game
import io.spacepiano.FirstScreen

class Game : Game() {
    override fun create() {
        setScreen(FirstScreen())
    }
}
