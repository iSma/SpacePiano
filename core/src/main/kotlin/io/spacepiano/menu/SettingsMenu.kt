package io.spacepiano.menu

class SettingsMenu(screen: MenuScreen) : Menu("Settings") {
    init {
        setItems(listOf(
                "123" to { },
                "XYZ" to { },
                "ABC" to { },
                "Back" to { screen.pop() }
        ))
    }
}