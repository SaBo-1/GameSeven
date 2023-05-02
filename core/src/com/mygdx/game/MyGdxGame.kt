package com.mygdx.game


import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2

class MyGdxGame : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var img: Texture
    private lateinit var playerSpriteSheet: Texture
    private lateinit var playerAnimation: Animation<TextureRegion>

    var playerX = 50f
    var playerY = 50f

    private var playerStateTime = 0f

    override fun create() {
        batch = SpriteBatch()
        img = Texture("background.png")
        playerSpriteSheet = Texture("one.png")

        // Erstelle eine TextureRegion-Matrix für den Spieler
        val tmp = TextureRegion.split(
            playerSpriteSheet,
            playerSpriteSheet.width / 4,  // Angenommen, es gibt 4 horizontale Frames
            playerSpriteSheet.height
        )

        // Erstelle die Animation
        val playerFrames = tmp[0]
        playerAnimation = Animation(0.1f, *playerFrames)

        //Steuerung
        Gdx.input.inputProcessor = MyInputProcessor(this)
    }

    override fun render() {
        // Aktualisiere die Spielzeit
        playerStateTime += Gdx.graphics.deltaTime

        // Hole den aktuellen Frame der Animation
        val currentFrame = playerAnimation.getKeyFrame(playerStateTime, true)

        ScreenUtils.clear(1f, 0f, 0f, 1f)
        batch.begin()
        batch.draw(img, 0f, 0f, 800f, 600f)
        batch.draw(currentFrame, playerX, playerY, currentFrame.regionWidth.toFloat(), currentFrame.regionHeight.toFloat()) // Zeichne den aktuellen Frame an der Spielerposition
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        img.dispose()
    }

    fun openMenu () {
        println("Menü würde sich öffnen.")
    }

    private var playerVelocity = Vector2(0f, 0f)

    fun movePlayer(x: Float, y: Float) {
        playerVelocity.set(x, y)
    }

    fun stopPlayerMovement() {
        playerVelocity.set(0f, 0f)
    }
}