package com.mygdx.game

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor

class MyInputProcessor(private val game: MyGdxGame) : InputProcessor {
    private val playerSpeed = 20000f

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.W -> game.movePlayer(0f, playerSpeed)
            Input.Keys.A -> game.movePlayer(-playerSpeed, 0f)
            Input.Keys.S -> game.movePlayer(0f, -playerSpeed)
            Input.Keys.D -> game.movePlayer(playerSpeed, 0f)
            Input.Keys.E -> game.openMenu()
        }
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D -> game.stopPlayerMovement()
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