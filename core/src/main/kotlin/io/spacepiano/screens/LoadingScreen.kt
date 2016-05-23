package io.spacepiano.screens

import io.spacepiano.R

class LoadingScreen : Screen() {
    init {
        R.loadAll()
    }

    override fun render(delta: Float) {
        super.render(delta)

        if (R.update()) {
            // Go to next screen
        }

        // Still loading
    }
}