package io.spacepiano.screens

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.utils.Align
import io.spacepiano.R
import io.spacepiano.actors.LaserArray
import io.spacepiano.actors.SpaceShip
import io.spacepiano.midi.*
import javax.sound.midi.ShortMessage


class GameScreen : Screen() {
    val syntRcv = Midi.synth.getReceiver()
    val track: Track
    val player: TrackPlayer
    val player2: TrackPlayer

    val laserArray = LaserArray()

    var speed = 1f

    init {
        val seq = R[R.midi["ode-to-joy"]]
        track = Midi.parser.parse(seq)[1]
        player = TrackPlayer(track)
        player2 = TrackPlayer(track)
        player2.seek(-3f)

        laserArray.setPosition(0f, -stage.height/2)
        laserArray.level = 4
        stage.addActor(laserArray)
    }

    fun shoot(message: ShortMessage) {
        laserArray.shoot(message)

        // TODO: Check timing, remove enemies, ...
        if (message.on) {

        } else if (message.off) {

        }
    }

    fun makeEntity(note: Note, bonus: Boolean = false): Actor {
        val entity =
                if (bonus)
                    makeBonus(note)
                else
                    makeEnemy(note)

        entity.setPosition((note.pitch - 60) * 32f, stage.height/2, Align.top)

        val action = Actions.sequence(
                Actions.moveBy(0f, -stage.height * 2, 8f),
                Actions.removeActor()
        )

        entity.addAction(action)
        return entity
    }

    private fun makeEnemy(note: Note): Actor {
        val color =
                if (note.black)
                    SpaceShip.Color.blue
                else
                    SpaceShip.Color.white

        val entity = SpaceShip.random(color, note.duration * 32 / 1000, 2)

        entity.setOrigin(Align.top)
        entity.rotateBy(180f)

        return entity
    }

    private fun makeBonus(note: Note): Actor {
        // TODO
        throw UnsupportedOperationException()
    }

    override fun render(delta: Float) {
        player.update(speed * delta)
        player2.update(speed * delta)

        player.notes.forEach {
            val entity = makeEntity(it)
            stage.addActor(entity)
        }

        player2.messages.forEach { syntRcv.send(it, 0) }
        player2.messages.forEach { shoot(it) }

        stage.act(speed * delta)
        stage.draw()
    }

    override fun dispose() {
        syntRcv.close()
    }
}