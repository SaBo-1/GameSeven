package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class GameSevenMain : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var img: Texture
    private lateinit var camera: OrthographicCamera
    private lateinit var player: Player

    private lateinit var enemyTexture1: Texture
    private lateinit var enemyTexture2: Texture
    private lateinit var enemyTexture3: Texture
    private lateinit var enemy1: Enemy
    private lateinit var enemy2: Enemy
    private lateinit var enemy3: Enemy

    // Startpunkt Einheiten
    private var playerX = 250f
    private var playerY = 250f
    private var enemy1X = 100f
    private var enemy1Y = 100f
    private var enemy2X = 200f
    private var enemy2Y = 200f
    private var enemy3X = 300f
    private var enemy3Y = 300f

    private var playerStateTime = 0f
    private var playerVelocity = Vector2(0f, 0f)

//--------------------------------------------------------
    override fun create() {
        batch = SpriteBatch()
        camera = OrthographicCamera()
        camera.setToOrtho(false, 800f, 480f)

        img = Texture("background.png")

        val playerSpriteSheet = Texture("one.png")
        player = Player(playerSpriteSheet, 4,1,0.13f)

        enemyTexture1 = Texture("one.png")
        enemyTexture2 = Texture("one.png")
        enemyTexture3 = Texture("one.png")
        enemy1 = Enemy(enemyTexture1, 4, 1, 0.13f)
        enemy2 = Enemy(enemyTexture2, 4, 1, 0.13f)
        enemy3 = Enemy(enemyTexture3, 4, 1, 0.13f)

        //Steuerung
        Gdx.input.inputProcessor = MyInputProcessor(this)
    }

//-----------------------------------------------------------
    override fun render() {
    super.render()

    // Aktualisiere die Kamera-Position
    camera.position.set(playerX, playerY, 0f)
    camera.update()

    // Aktualisiere die Spielzeit
    playerStateTime += Gdx.graphics.deltaTime

    // Aktualisiere die Spielerposition
    val oldPlayerX = playerX
    val oldPlayerY = playerY
    playerX += playerVelocity.x * Gdx.graphics.deltaTime
    playerY += playerVelocity.y * Gdx.graphics.deltaTime

    // Hole den aktuellen Frame der Animation
    val currentFrame = player.animation.getKeyFrame(playerStateTime, true)
    val enemy1Frame = enemy1.animation.getKeyFrame(playerStateTime, true)
    val enemy2Frame = enemy2.animation.getKeyFrame(playerStateTime, true)
    val enemy3Frame = enemy3.animation.getKeyFrame(playerStateTime, true)

    // Aktualisiere die Positionen der Kollisionsboxen
    val boxWidth = currentFrame.regionWidth * 0.2f  // 30 % der Sprite-Breite
    val boxHeight = currentFrame.regionHeight * 0.2f  // 30 % der Sprite-Höhe
    val offsetX = (currentFrame.regionWidth - boxWidth) / 2f  // X-Offset, um die Box horizontal zu zentrieren
    val offsetY = (currentFrame.regionHeight - boxHeight) / 2f  // Y-Offset, um die Box vertikal zu zentrieren

    player.collisionBox.set(playerX + offsetX, playerY + offsetY, boxWidth, boxHeight)

    //player.collisionBox.set(playerX, playerY, currentFrame.regionWidth.toFloat(), currentFrame.regionHeight.toFloat())
    enemy1.collisionBox.set(enemy1X, enemy1Y, enemy1Frame.regionWidth.toFloat(), enemy1Frame.regionHeight.toFloat())
    enemy2.collisionBox.set(enemy2X, enemy2Y, enemy2Frame.regionWidth.toFloat(), enemy2Frame.regionHeight.toFloat())
    enemy3.collisionBox.set(enemy3X, enemy3Y, enemy3Frame.regionWidth.toFloat(), enemy3Frame.regionHeight.toFloat())

    // Kollisionserkennung
    if (player.collisionBox.overlaps(enemy1.collisionBox) ||
        player.collisionBox.overlaps(enemy2.collisionBox) ||
        player.collisionBox.overlaps(enemy3.collisionBox)
    ) {
        // Kollision aufgetreten, führen Sie entsprechende Aktionen aus
        println("Kollision erkannt!")
        // Setze die Spielerposition zurück
        playerX = oldPlayerX
        playerY = oldPlayerY
        player.collisionBox.setPosition(playerX, playerY)
    }

    ScreenUtils.clear(1f, 0f, 0f, 1f)

    // Rendern Sie Ihre Spielobjekte
    batch.projectionMatrix = camera.combined

    batch.begin()
    batch.draw(img, 0f, 0f, 1400f, 900f)
    batch.draw(enemy1Frame, enemy1X, enemy1Y, enemy1Frame.regionWidth.toFloat(), enemy1Frame.regionHeight.toFloat())
    batch.draw(enemy2Frame, enemy2X, enemy2Y, enemy2Frame.regionWidth.toFloat(), enemy2Frame.regionHeight.toFloat())
    batch.draw(enemy3Frame, enemy3X, enemy3Y, enemy3Frame.regionWidth.toFloat(), enemy3Frame.regionHeight.toFloat())
    batch.draw(currentFrame, playerX, playerY, currentFrame.regionWidth.toFloat(), currentFrame.regionHeight.toFloat()) // Zeichne den aktuellen Frame an der Spielerposition
    batch.end()
}


    override fun dispose() {
        batch.dispose()
        img.dispose()
        enemyTexture1.dispose()
        enemyTexture2.dispose()
        enemyTexture3.dispose()
    }

    fun openMenu () {
        println("Menü würde sich öffnen.")
    }

    fun movePlayer(x: Float, y: Float) {
        playerVelocity.set(x, y)
    }

}