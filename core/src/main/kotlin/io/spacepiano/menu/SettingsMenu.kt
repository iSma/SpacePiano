package io.spacepiano.menu

class SettingsMenu(screen: MenuScreen) : Menu("Settings") {
    init {
        setItems(listOf(
                "List MIDI devices" to { screen.push(DeviceListMenu(screen)) },
                "Test MIDI" to { screen.push(MidiTestMenu(screen)) },
                "<Back" to { screen.pop() }
        ))
    }
}