package io.spacepiano.menu

import io.spacepiano.midi.Midi

class DeviceListMenu(val screen: MenuScreen) : Menu("MIDI Devices", menuStyle = "menu-small") {
    init {
        val items = Midi.outputDevices
                .map { it.deviceInfo.name to { } }
                .plus("<Back" to { screen.pop() })

        setItems(items)
    }
}