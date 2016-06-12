package io.spacepiano.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import io.spacepiano.R
import java.util.*

class SpaceShip(val color: Color, val parts: List<List<String>>) : Transformable() {
    constructor(color: Color, partSpec: String) : this(color, partSpec.split(";").map { it.trim().split(" ") })

    enum class Color {blue, green, yellow, white }

    val sprites: List<Sprite>

    init {
        val atlas = R[R.ships[color.name]]

        val sprites = mutableListOf<Sprite>()

        val error = parts.fold(false) { error, stage ->
            error || stage.fold(error) { error, part ->
                if (atlas.findRegion(part) === null) {
                    Gdx.app.error("SpaceShip", "Unknown part name $part")
                    true
                } else error
            }
        }

        val parts = if (error) emptyList() else parts

        var maxX = 0f
        var y = 0f
        for (stage in parts) {
            if (stage.isEmpty())
                continue

            val center = atlas.createSprite(stage[0])
            center.setOriginCenter()
            center.translate(-center.originX, -center.height)

            center.translate(0f, y)
            sprites.add(center)

            y -= center.height
            var x = center.width / 2
            var stageY = y
            for (i in 1..stage.size - 1) {
                val left = atlas.createSprite(stage[i])
                left.setOriginCenter()

                val right = atlas.createSprite(stage[i])
                right.setOriginCenter()
                right.setFlip(true, false)

                if (stage[i] == "wing-14") {
                    left.flip(true, false)
                    right.flip(true, false)
                    stageY -= 16f
                }

                if (stage[i] in listOf("wing-07", "wing-08", "wing-09")) {
                    left.translate(0f, -8f)
                    right.translate(0f, -8f)
                }

                left.translate(-x - left.width, stageY)
                right.translate(+x, stageY)

                sprites.add(left)
                sprites.add(right)
                x += left.width
            }

            maxX = Math.max(x, maxX)
        }

        sprites.forEach { it.translate(maxX, -y) }
        width = maxX * 2
        height = -y
        debug = true
        this.sprites = sprites
    }

    override fun drawTransformed(batch: Batch?, parentAlpha: Float) = sprites.forEach { it.draw(batch) }

    companion object {
        private val N_COCKPITS = 14
        private val N_ENGINES = 21
        private val N_FUSELAGES = 12
        private val N_WEAPONS = 15
        private val N_WINGS = 14

        private val rand = Random()

        private fun randomPart(type: String): String {
            val N = when (type) {
                "cockpit" -> N_COCKPITS
                "fuselage" -> N_FUSELAGES
                "wing" -> N_WINGS
                "weapon" -> N_WEAPONS
                "engine" -> N_ENGINES
                else -> 0
            }

            val k = rand.nextInt(N) + 1
            return "$type-%02d".format(k)
        }

        fun random(color: Color, length: Int, width: Int): SpaceShip {
            val parts = mutableListOf<List<String>>()

            val cockpit = (1..1).map { randomPart("cockpit") }

            parts.add(cockpit)

            val body = (1..length).map {
                val w = rand.nextInt(width) + 1
                (1..w).map {
                    when (it) {
                        1 -> randomPart("fuselage")
                        w - 1 -> randomPart("weapon")
                        w -> randomPart("wing")
                        else -> when (rand.nextInt(4)) {
                            0 -> randomPart("weapon")
                            1 -> randomPart("wing")
                            else -> randomPart("fuselage")
                        }
                    }
                }
            }

            parts.addAll(body)

            val nEngines = parts.last().count { it.startsWith("fuselage") }
            val engines = (1..nEngines).map { randomPart("engine") }

            parts.add(engines)

            return SpaceShip(color, parts)
        }
    }
}