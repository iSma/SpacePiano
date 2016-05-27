package io.spacepiano.menu

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import io.spacepiano.R
import com.badlogic.gdx.scenes.scene2d.ui.List as UiList

open class Menu(title: String, items: List<Pair<String, () -> Unit>> = listOf()) : Table(R[R.SKIN]) {
    val title = Label(title, R[R.SKIN], "title")
    val list = com.badlogic.gdx.scenes.scene2d.ui.List<String>(R[R.SKIN], "menu")
    val scroll = ScrollPane(list)

    val items: MutableList<String> = mutableListOf<String>()
    val actions: MutableList<() -> Unit> = mutableListOf()

    val clickListener = object : ClickListener() {
        override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
            super.touchUp(event, x, y, pointer, button)
            runSelected()
        }
    }

    val keyListener = object : InputAdapter() {
        override fun keyDown(keycode: Int): Boolean {
            when (keycode) {
                Input.Keys.UP -> moveSelectionBy(-1)
                Input.Keys.DOWN -> moveSelectionBy(+1)
                else -> return false
            }

            return true
        }

        override fun keyUp(keycode: Int): Boolean {
            when (keycode) {
                Input.Keys.ENTER -> runSelected()
                Input.Keys.ESCAPE -> { selectLast(); runSelected() }
                Input.Keys.HOME -> { selectFirst() }
                Input.Keys.END -> { selectLast() }
                else -> return false
            }

            return true
        }
    }

    init {
        setItems(items)
        padTop(32f).align(Align.top).add(this.title).row()
        add(scroll).align(Align.left).padTop(32f)

        list.addListener(clickListener)
    }

    fun setItems(items: List<Pair<String, () -> Unit>>){
        items.forEach {
            this.items.add(it.first)
            this.actions.add(it.second)
        }
        this.refresh()
    }

    fun refresh() {
        val array = Array<String>(items.size)
        items.forEach { array.add(it) }
        list.setItems(array)
    }


    private fun runSelected() {
        if (list.selectedIndex > 0 && list.selectedIndex <= items.size)
            actions[list.selectedIndex]()
    }

    private fun selectFirst() {
        list.selectedIndex = 0
    }

    private fun selectLast() {
        list.selectedIndex = items.size - 1
    }

    private fun moveSelectionBy(direction: Int) {
        list.selectedIndex = (list.selectedIndex + items.size + direction) % items.size
    }
}

