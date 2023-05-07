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
    private lateinit var enemy1: Enemy

    // Startpunkt Einheiten
    private var playerX = 450f
    private var playerY = 450f
    private var enemy1X = 300f
    private var enemy1Y = 300f


    private var playerStateTime = 0f
    private var playerVelocity = Vector2(0f, 0f)

//--------------------------------------------------------
    override fun create() {
        batch = SpriteBatch()
        camera = OrthographicCamera()
        camera.setToOrtho(false, 800f, 480f)

        img = Texture("artwork.png")

        val playerSpriteSheet = Texture("one.png")
        player = Player(playerSpriteSheet, 4,1,0.13f)

        val enemyTexture1 = Texture("two.png")
        enemy1 = Enemy(enemyTexture1, 4, 1, 0.13f, moveSpeed = 1000f, level = 3, lootTable = createSampleLootTable())
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

    // Aktualisiere die Positionen der Kollisionsboxen
    val boxWidth = currentFrame.regionWidth * 0.2f  // 30 % der Sprite-Breite
    val boxHeight = currentFrame.regionHeight * 0.2f  // 30 % der Sprite-Höhe
    val offsetX = (currentFrame.regionWidth - boxWidth) / 2f  // X-Offset, um die Box horizontal zu zentrieren
    val offsetY = (currentFrame.regionHeight - boxHeight) / 2f  // Y-Offset, um die Box vertikal zu zentrieren

    player.collisionBox.set(playerX + offsetX, playerY + offsetY, boxWidth, boxHeight)

    // Aktualisiere die Position der Gegner
    val (newEnemy1X, newEnemy1Y) = updateEnemyPosition(enemy1, enemy1X, enemy1Y, playerX, playerY)

    // Aktualisiere enemy1X und enemy1Y mit den neuen berechneten Positionen
    enemy1X = newEnemy1X
    enemy1Y = newEnemy1Y

    // Aktualisiere die Kollisionsboxen der Gegner
    enemy1.collisionBox.set(newEnemy1X, newEnemy1Y, enemy1Frame.regionWidth.toFloat(), enemy1Frame.regionHeight.toFloat())

    // Kollisionserkennung
    val collidedWithEnemy1 = player.collisionBox.overlaps(enemy1.collisionBox)


    if (collidedWithEnemy1) {
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
    batch.draw(enemy1Frame, newEnemy1X, newEnemy1Y, enemy1Frame.regionWidth.toFloat(), enemy1Frame.regionHeight.toFloat())
    batch.draw(currentFrame, playerX, playerY, currentFrame.regionWidth.toFloat(), currentFrame.regionHeight.toFloat()) // Zeichne den aktuellen Frame an der Spielerposition
    batch.end()
}

    override fun dispose() {
        batch.dispose()
        img.dispose()
        enemyTexture1.dispose()
    }

    fun openMenu () {
        println("Menü würde sich öffnen.")
    }

    fun createSampleLootTable(): LootTable {
        val commonItem = Item(name = "Common Item", type = ItemType.WEAPON, rarity = ItemRarity.COMMON, statBoosts = mapOf(), effects = listOf())
        val rareItem = Item(name = "Rare Item", type = ItemType.ARMOR, rarity = ItemRarity.RARE, statBoosts = mapOf(), effects = listOf())
        val legendaryItem = Item(name = "Legendary Item", type = ItemType.ACCESSORY, rarity = ItemRarity.LEGENDARY, statBoosts = mapOf(), effects = listOf())

        val lootEntries = listOf(
                LootEntry(commonItem, weight = 80),
                LootEntry(rareItem, weight = 15),
                LootEntry(legendaryItem, weight = 5)
        )

        return LootTable(lootEntries)
    }

    fun movePlayer(x: Float, y: Float) {
        playerVelocity.set(x * player.moveSpeed, y * player.moveSpeed)
    }

    private fun updateEnemyPosition(enemy: Enemy, enemyX: Float, enemyY: Float, playerX: Float, playerY: Float): Pair<Float, Float> {
        val deltaTime = Gdx.graphics.deltaTime
        val direction = Vector2(playerX - enemyX, playerY - enemyY).nor()
        val distance = enemy.moveSpeed * deltaTime

        val newEnemyX = enemyX + direction.x * distance
        val newEnemyY = enemyY + direction.y * distance

        return Pair(newEnemyX, newEnemyY)
    }

}