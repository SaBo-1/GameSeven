package com.mygdx.game

// Hier werden alles Funktionen Importiert
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Vector2
import kotlin.math.sqrt

// Das ist die Main
class MyInputProcessor(private val game: GameSevenMain) : InputProcessor {

    // Hier kann die Spieler Bewegungsgeschwindigkeit angepasst werden.
    private val playerSpeed = 150f

    // Boost oder Schub
    private val boostFactor = 2f
    private var isBoosting = false

    // Speichert die gedrückten Tasten und ermöglicht ein seitwärts Bewegung.
    private val pressedKeys = HashSet<Int>()

    // Beginn Steuerung
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
        // sieht Komisch aus funktioniert aber^^
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

    // Hier wird die Tastatur eingabe überschrieben,
    // damit nicht nur eine Taste auf einmal gedrückt werden kann.
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

    // überschreiben beim Loslassen der Tastatur
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

        // Boost, soll mal eine klassische Rolle werden.
        if (keycode == Input.Keys.SPACE) {
            isBoosting = false
            updatePlayerMovement()
        }

        return false
    }

    // für später
    override fun keyTyped(character: Char): Boolean {
        return false
    }

    // auch für später
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    // immer noch für später
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    // wie man sieht, ist alles vorbereitet.
    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    // hier wird auch jeder Kommentar gelesen
    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }

    // Die Kugel wir abgeschossen
    // Aktuell nur eine der code ist für mehrere ausgelegt.
    private fun shootProjectile() {
        val playerPosition = Vector2(game.playerX, game.playerY)
        val mousePosition = game.getMouseWorldPosition()
        val direction = mousePosition.sub(playerPosition).nor()
        // geschwindigkeit der Kugel
        val bulletSpeed = 500f

        // Die Aktuelle eine Kugel
        val projectile = Projectile(
            game.bulletTexture,
            frameCols = 10,
            frameRows = 1,
            animationDuration = 0.05f,
            position = playerPosition.cpy(),
            velocity = direction.scl(bulletSpeed),
            damage = 10
        )
        game.projectiles.add(projectile)
    }

    // Mit der Linken Maus Taste wird der Hauptangriff, abgeschossen
    // nur einmal per KLick
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (button == Input.Buttons.LEFT) {
            shootProjectile()
        }
        return false
    }
}