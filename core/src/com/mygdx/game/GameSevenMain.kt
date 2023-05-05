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
    private var playerX = 450f
    private var playerY = 450f
    private var enemy1X = 300f
    private var enemy1Y = 300f
    private var enemy2X = 400f
    private var enemy2Y = 400f
    private var enemy3X = 600f
    private var enemy3Y = 300f

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

        enemyTexture1 = Texture("two.png")
        enemyTexture2 = Texture("drei.png")
        enemyTexture3 = Texture("vier.png")
        enemy1 = Enemy(enemyTexture1, 4, 1, 0.13f, moveSpeed = 1000f, level = 3, lootTable = createSampleLootTable())
        enemy2 = Enemy(enemyTexture2, 4, 1, 0.13f, moveSpeed = 100f, level = 2, lootTable = createSampleLootTable())
        enemy3 = Enemy(enemyTexture3, 4, 1, 0.13f, moveSpeed = 0.1f, level = 1, lootTable = createSampleLootTable())

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

    // Aktualisiere die Position der Gegner
    val (newEnemy1X, newEnemy1Y) = updateEnemyPosition(enemy1, enemy1X, enemy1Y, playerX, playerY)
    val (newEnemy2X, newEnemy2Y) = updateEnemyPosition(enemy2, enemy2X, enemy2Y, playerX, playerY)
    val (newEnemy3X, newEnemy3Y) = updateEnemyPosition(enemy3, enemy3X, enemy3Y, playerX, playerY)

    // Aktualisiere die Kollisionsboxen der Gegner
    enemy1.collisionBox.set(newEnemy1X, newEnemy1Y, enemy1Frame.regionWidth.toFloat(), enemy1Frame.regionHeight.toFloat())
    enemy2.collisionBox.set(newEnemy2X, newEnemy2Y, enemy2Frame.regionWidth.toFloat(), enemy2Frame.regionHeight.toFloat())
    enemy3.collisionBox.set(newEnemy3X, newEnemy3Y, enemy3Frame.regionWidth.toFloat(), enemy3Frame.regionHeight.toFloat())

    // Kollisionserkennung
    val collidedWithEnemy1 = player.collisionBox.overlaps(enemy1.collisionBox)
    val collidedWithEnemy2 = player.collisionBox.overlaps(enemy2.collisionBox)
    val collidedWithEnemy3 = player.collisionBox.overlaps(enemy3.collisionBox)

    if (collidedWithEnemy1 || collidedWithEnemy2 || collidedWithEnemy3) {
        // Kollision aufgetreten, führen Sie entsprechende Aktionen aus
        println("Kollision erkannt!")

        // Setze die Spielerposition zurück
        playerX = oldPlayerX
        playerY = oldPlayerY
        player.collisionBox.setPosition(playerX, playerY)

        // Füge dem Spieler Schaden zu
        val damage = if (collidedWithEnemy1) enemy1.attack else if (collidedWithEnemy2) enemy2.attack else enemy3.attack
        player.takeDamage(damage)
        println("Spieler hat Schaden erlitten. Aktuelle Lebenspunkte: ${player.hitPoints}")
    }

    ScreenUtils.clear(1f, 0f, 0f, 1f)

    // Rendern Sie Ihre Spielobjekte
    batch.projectionMatrix = camera.combined

    batch.begin()
    batch.draw(img, 0f, 0f, 1400f, 900f)
    batch.draw(enemy1Frame, newEnemy1X, newEnemy1Y, enemy1Frame.regionWidth.toFloat(), enemy1Frame.regionHeight.toFloat())
    batch.draw(enemy2Frame, newEnemy2X, newEnemy2Y, enemy2Frame.regionWidth.toFloat(), enemy2Frame.regionHeight.toFloat())
    batch.draw(enemy3Frame, newEnemy3X, newEnemy3Y, enemy3Frame.regionWidth.toFloat(), enemy3Frame.regionHeight.toFloat())
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
        println("UpdateEnemyAufruf")
        if (enemy == enemy3) {
            return updateEnemy3PatrolPosition(enemyX, enemyY)
        }
        val direction = Vector2(playerX - enemyX, playerY - enemyY).nor()
        var newX = enemyX + direction.x * enemy.moveSpeed * Gdx.graphics.deltaTime
        var newY = enemyY + direction.y * enemy.moveSpeed * Gdx.graphics.deltaTime

        //  Debugging
        println("Gegner-Bewegungsrichtung: $direction, Geschwindigkeit: ${enemy.moveSpeed}, neue Position: ($newX, $newY)")

        return Pair(newX, newY)
    }

    private val enemy3PatrolPoints = listOf(
            Vector2(600f, 300f),
            Vector2(700f, 300f)
    )
    private var enemy3CurrentPatrolPointIndex = 0

    private fun updateEnemy3PatrolPosition(enemyX: Float, enemyY: Float): Pair<Float, Float> {
        val currentPatrolPoint = enemy3PatrolPoints[enemy3CurrentPatrolPointIndex]
        val direction = Vector2(currentPatrolPoint.x - enemyX, currentPatrolPoint.y - enemyY).nor()
        var newX = enemyX + direction.x * enemy3.moveSpeed * Gdx.graphics.deltaTime
        var newY = enemyY + direction.y * enemy3.moveSpeed * Gdx.graphics.deltaTime

        val distanceToPatrolPoint = Vector2(newX - currentPatrolPoint.x, newY - currentPatrolPoint.y).len()
        if (distanceToPatrolPoint <= 5f) { // Wenn der Gegner nahe genug am Patrouillenpunkt ist
            enemy3CurrentPatrolPointIndex = (enemy3CurrentPatrolPointIndex + 1) % enemy3PatrolPoints.size // Wechsel zum nächsten Patrouillenpunkt
        }

        return Pair(newX, newY)
    }
}