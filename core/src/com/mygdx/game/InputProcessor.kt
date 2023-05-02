package com.mygdx.game

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import kotlin.math.sqrt

class MyInputProcessor(private val game: GameSevenMain) : InputProcessor {

    // Hier kann die Spieler geschwindigkeit angepasst werden.
    private val playerSpeed = 150f

    // Boost oder Schub
    private val boostFactor = 2f
    private var isBoosting = false

    // Speichert die gedrückten Tasten und ermöglicht ein seitwärts Bewegung.
    private val pressedKeys = HashSet<Int>()

    private fun updatePlayerMovement() {
        var x = 0f
        var y = 0f

        //   Steuerung mit W, A, S, D und den Pfeiltasten.
        if (pressedKeys.contains(Input.Keys.W)) y += playerSpeed
        if (pressedKeys.contains(Input.Keys.UP)) y += playerSpeed

        if (pressedKeys.contains(Input.Keys.A)) x -= playerSpeed
        if (pressedKeys.contains(Input.Keys.LEFT)) x -= playerSpeed

        if (pressedKeys.contains(Input.Keys.S)) y -= playerSpeed
        if (pressedKeys.contains(Input.Keys.DOWN)) y -= playerSpeed

        if (pressedKeys.contains(Input.Keys.D)) x += playerSpeed
        if (pressedKeys.contains(Input.Keys.RIGHT)) x += playerSpeed

        // Normalisiere die Geschwindigkeit, um eine gleichmäßige Geschwindigkeit in alle Richtungen zu gewährleisten
        if (x != 0f && y != 0f) {
            val length = sqrt(x * x + y * y)
            x /= length
            y /= length
            x *= playerSpeed
            y *= playerSpeed
        }

        // Boost anwenden
        if (isBoosting) {
            x *= boostFactor
            y *= boostFactor
        }

        game.movePlayer(x, y)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode in listOf(
                Input.Keys.W,
                Input.Keys.A,
                Input.Keys.S,
                Input.Keys.D,
                Input.Keys.UP,
                Input.Keys.DOWN,
                Input.Keys.LEFT,
                Input.Keys.RIGHT,
            )) {
            pressedKeys.add(keycode)
            updatePlayerMovement()
        }

        if (keycode == Input.Keys.E) {
            game.openMenu()
        }

        if (keycode == Input.Keys.SPACE) {
            isBoosting = true
            updatePlayerMovement()
        }


        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        if (keycode in listOf(
                Input.Keys.W,
                Input.Keys.A,
                Input.Keys.S,
                Input.Keys.D,
                Input.Keys.UP,
                Input.Keys.DOWN,
                Input.Keys.LEFT,
                Input.Keys.RIGHT,
            )) {
            pressedKeys.remove(keycode)
            updatePlayerMovement()
        }

        if (keycode == Input.Keys.SPACE) {
            isBoosting = false
            updatePlayerMovement()
        }

        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }
}