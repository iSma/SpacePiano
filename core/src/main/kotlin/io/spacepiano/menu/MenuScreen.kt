package io.spacepiano.menu

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Align
import io.spacepiano.screens.Screen
import java.util.*

open class MenuScreen: Screen() {
    override val input = InputMultiplexer(stage)

    val stack = Stack<Menu>()

    val FADE_DURATION = .3f

    val pushInAction: Action
        get() = Actions.sequence(
                Actions.hide(),
                Actions.moveToAligned(+stage.width, 0f, Align.center),
                Actions.alpha(0f),
                Actions.show(),
                Actions.parallel(
                        Actions.fadeIn(FADE_DURATION, Interpolation.fade),
                        Actions.moveToAligned(0f, 0f, Align.center, FADE_DURATION, Interpolation.pow4Out)
                )
        )

    val pushOutAction : Action
        get() = Actions.sequence(
                Actions.parallel(
                        Actions.fadeOut(FADE_DURATION, Interpolation.pow4Out),
                        Actions.moveToAligned(-stage.width, 0f, Align.center, FADE_DURATION, Interpolation.pow4Out)
                ),
                Actions.hide()
        )

    val popInAction: Action
        get() = Actions.sequence(
                Actions.show(),
                Actions.parallel(
                        Actions.fadeIn(FADE_DURATION, Interpolation.fade),
                        Actions.moveToAligned(0f, 0f, Align.center, FADE_DURATION, Interpolation.pow4Out)
                )
        )

    val popOutAction : Action
        get() = Actions.parallel(
                Actions.fadeOut(FADE_DURATION, Interpolation.pow4Out),
                Actions.moveToAligned(+stage.width, 0f, Align.center, FADE_DURATION, Interpolation.pow4Out)
        )

    fun initMenu(menu: Menu) {
        menu.width = stage.width
        menu.height = stage.height
        menu.setOrigin(Align.center)
        menu.setPosition(0f, 0f, Align.center)
    }

    fun push(menu: Menu, inAction: Action = pushInAction) {
        if (!stack.empty()) {
            val last = stack.peek()
            last.addAction(pushOutAction)
            input.removeProcessor(last.keyListener)
        }

        initMenu(menu)
        stage.addActor(menu)
        stack.push(menu)

        menu.addAction(addKeyListenerAction(inAction))
    }

    fun pop(outAction: Action = popOutAction) {
        if (stack.empty())
            return

        val last = stack.pop()
        input.removeProcessor(last.keyListener)
        val action = Actions.sequence(outAction, Actions.removeActor())
        last.addAction(action)

        if (!stack.empty())
            stack.peek().addAction(addKeyListenerAction(popInAction))
    }

    private fun addKeyListenerAction(action: Action) =
            Actions.sequence(action, Actions.run { input.addProcessor(stack.peek().keyListener) })
}