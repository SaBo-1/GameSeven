package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3

class GameSevenMain : ApplicationAdapter() {

    // Warum Late Init?
    // Er wird verwendet um, einer Eigenschaft die möglichkeit zu geben,
    // später im code erstellt zu werden. Verbessert auch oft nur die Lesbarkeit.
    private lateinit var batch: SpriteBatch // wird zu rendern mehrerer Sprites benötigt
    private lateinit var img: Texture // Lässt Texturen Laden
    private lateinit var camera: OrthographicCamera // erstellung der Kamera
    private lateinit var player: Player // Erstellung des Spielers
    private lateinit var crosshairTexture: Texture // Das Fadenkreuz
    private lateinit var enemyTexture1: Texture // Die erste Gegner-Textur.
    private lateinit var enemy1: Enemy // erstellt den ersten Gegner
    private lateinit var bulletTexture: Texture // Die grüne Kugel Textur wird erstellt
    private lateinit var chestTexture: Texture // Die Aktuellen Gems später Truhen Textur

    // Die veränderbaren Listen von Truhe und fern Attacken
    val projectiles = mutableListOf<Projectile>()
    val chests = mutableListOf<Chest>()

    // Shake it Baby, lässt bei treffern die Kamera wackeln
    private var shakeDuration = 0f
    private val shakeDurationMax = 0.5f
    private val shakeIntensity = 10f
    private var shakingCamera = false

    // Startpunkt vom Spieler und der Gegner
    var playerX = 450f
    var playerY = 450f
    private var enemy1X = 300f
    private var enemy1Y = 300f

    // Setzt die Spieler Zeit sowie den Vector fest am Anfang des Spiels ein.
    private var playerStateTime = 0f
    private var playerVelocity = Vector2(0f, 0f)

    // Hier startet die Create Funktion.
    // Was ist die Create Funktion?
    // Grafiken, Sounds, Musik, Schriften und vieles mehr werden hier erstellt.
    // Create kreieren trifft es am besten ^^
    // irgendwie schon Selbsterklärend oder?
//--------------------------------------------------------
    override fun create() {
        batch = SpriteBatch() // Lässt Animationen zu
        camera = OrthographicCamera() // Erstellt die Kamera
        // hier kann man, den Kamerawinkel quasi die Höhe bzw. Sichtwinkel einstellen.
        camera.setToOrtho(false, 800f, 480f)

        // Der Hintergrund
        img = Texture("artwork.png")
        // Die Aktuell eine grüne Kugel
        bulletTexture = Texture("greenbullet.png")
        // Das Fadenkreuz
        crosshairTexture = Texture("crosshair.png")
        // Die Optik des Spielers
        val playerSpriteSheet = Texture("one.png")
        // Die aktuelle Kiste die ein Diamant ist
        chestTexture = Texture("gemBlue.png")
        // Hier muss man die Frames des Sprites einstellen
        player = Player(playerSpriteSheet, 4,1,0.13f)
        // Die Optik des Gegners
        val enemyTexture1 = Texture("two.png")
        // Die Frames einstellung der Gegner Animation
        enemy1 = Enemy(enemyTexture1, 4, 1, 0.13f, moveSpeed = 1f, level = 10, lootTable = createSampleLootTable())
        // Hier wird die Steuerung vom InputProcessor geladen
        Gdx.input.inputProcessor = MyInputProcessor(this)
    }

//--------------------------------------------------------
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
    val boxWidth = currentFrame.regionWidth * 0.2f  // 20 % der Sprite-Breite
    val boxHeight = currentFrame.regionHeight * 0.2f  // 20 % der Sprite-Höhe
    val offsetX = (currentFrame.regionWidth - boxWidth) / 2f  // Box horizontal zu zentrieren
    val offsetY = (currentFrame.regionHeight - boxHeight) / 2f  // Box vertikal zu zentrieren

    player.collisionBox.set(playerX + offsetX, playerY + offsetY, boxWidth, boxHeight)

    // Aktualisiere die Position und die Kollisionsbox des Gegners
    val (newEnemy1X, newEnemy1Y) = updateEnemyPosition(enemy1, enemy1X, enemy1Y, playerX, playerY)
    enemy1X = newEnemy1X
    enemy1Y = newEnemy1Y
    enemy1.collisionBox.set(newEnemy1X, newEnemy1Y, enemy1Frame.regionWidth.toFloat(), enemy1Frame.regionHeight.toFloat())

    // Kollisionserkennung mit dem Enemy
    val collidedWithEnemy1 = player.collisionBox.overlaps(enemy1.collisionBox)
    if (collidedWithEnemy1) {
        // Kollision aufgetreten, führen Sie entsprechende Aktionen aus
        println("Kollision erkannt!")

        // Setze die Spielerposition zurück
        playerX = oldPlayerX
        playerY = oldPlayerY
        player.collisionBox.setPosition(playerX, playerY)

        // Berechne die Rückstoßrichtung
        val knockbackDirection = Vector2(playerX - enemy1X, playerY - enemy1Y).nor()

        // Multipliziere die Richtung mit der gewünschten Rückstoßstärke
        val knockbackStrength = 50f
        val knockback = Vector2(knockbackDirection.x * knockbackStrength, knockbackDirection.y * knockbackStrength)

        // Aktualisiere die Position des Feindes
        enemy1X += knockback.x
        enemy1Y += knockback.y
        enemy1.collisionBox.setPosition(enemy1X, enemy1Y)

        // Aktiviere Kamera-Shake
        shakingCamera = true
        shakeDuration = shakeDurationMax

        // Füge dem Spieler Schaden zu
        val damageToPlayer = enemy1.attack - player.defense
        player.takeDamage(damageToPlayer)
        println("Spieler hat Schaden erhalten! Verbleibende Gesundheit: ${player.hitPoints}")

        if (player.hitPoints <= 0) {
            // Führen Sie die gewünschten Aktionen aus, wenn der Spieler tot ist
            println("Spieler ist tot!")
        }

    }

    // Schüttelt die Kamera
    if (shakingCamera) {
        // Reduziere die verbleibende Shake-Dauer
        shakeDuration -= Gdx.graphics.deltaTime

        // Berechne eine zufällige Verschiebung für die Kamera basierend auf der Shake-Intensität
        val randomX = MathUtils.random(-shakeIntensity, shakeIntensity)
        val randomY = MathUtils.random(-shakeIntensity, shakeIntensity)

        // Wende die Verschiebung auf die Kamera an
        camera.position.x += randomX
        camera.position.y += randomY

        // Beende den Kamera-Shake, wenn die Dauer abgelaufen ist
        if (shakeDuration <= 0f) {
            shakingCamera = false
            shakeDuration = 0f
        }
    }
        camera.update()

    // Aufnahme der Kiste
    val chestIterator = chests.iterator()
    while (chestIterator.hasNext()) {
        val chest = chestIterator.next()
        if (player.collisionBox.overlaps(chest.collisionBox)) {
            println("Spieler hat ${chest.item.name} aufgenommen.")
            // Add the item to the player's inventory (not implemented in the provided code)
            // player.inventory.addItem(chest.item)
            chestIterator.remove() // Remove the chest from the list
        }
    }

    ScreenUtils.clear(1f, 0f, 0f, 1f)

    val mousePos = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
    camera.unproject(mousePos)

    // Aktualisieren der Projektile
    val iterator = projectiles.iterator()
    while (iterator.hasNext()) {
        val projectile = iterator.next()
        projectile.update(Gdx.graphics.deltaTime)

        // Kollisionserkennung zwischen Projektil und Feind
        if (projectile.collisionBox.overlaps(enemy1.collisionBox)) {
            enemy1.hitPoints -= projectile.damage // Füge dem Feind Schaden zu
            iterator.remove() // Entferne das Projektil
            println("Feind getroffen! Verbleibende Gesundheit: ${enemy1.hitPoints}")

            if (enemy1.isDead()) {
                // Führen Sie die gewünschten Aktionen aus, wenn der Feind tot ist
                println("Feind ist tot!")
                val loot = enemy1.dropItem()
                if (loot != null) {
                    val chest = Chest(chestTexture, Vector2(enemy1X, enemy1Y), loot)
                    chests.add(chest)
                    player.gainExperience(10)
                    println("Feind hat ${loot.name} fallen gelassen.")
                } else {
                    println("Feind hat kein Item fallen gelassen.")
                }
                // Entfernen Sie den Feind aus dem Spiel (z.B. durch Setzen seiner Position außerhalb des Bildschirms oder Entfernen aus der Liste der aktiven Feinde)
                // hier wird die Position des Feindes außerhalb des Bildschirms gesetzt, als Beispiel:
                enemy1X = -1000f
                enemy1Y = -1000f
                enemy1.collisionBox.setPosition(enemy1X, enemy1Y)
            }
        } else {
            // Entfernen der Projektile, die außerhalb des Bildschirms sind (optional)
            if (projectile.position.x < 0 || projectile.position.x > 1400 || projectile.position.y < 0 || projectile.position.y > 900) {
                iterator.remove()
            }
        }
    }

    // Rendern -----------------------------------------------------------------------------------
    batch.projectionMatrix = camera.combined

    batch.begin()
    batch.draw(img, 0f, 0f, 1400f, 900f)
    batch.draw(enemy1Frame, newEnemy1X, newEnemy1Y, enemy1Frame.regionWidth.toFloat(), enemy1Frame.regionHeight.toFloat())
    batch.draw(currentFrame, playerX, playerY, currentFrame.regionWidth.toFloat(), currentFrame.regionHeight.toFloat())
    batch.draw(crosshairTexture, mousePos.x - crosshairTexture.width / 2, mousePos.y - crosshairTexture.height / 2)
    projectiles.forEach { projectile ->
        val frame = projectile.animation.getKeyFrame(playerStateTime, true)
        batch.draw(frame, projectile.position.x, projectile.position.y)
    }
    projectiles.forEach { projectile ->
        val frame = projectile.animation.getKeyFrame(playerStateTime, true)
        batch.draw(frame, projectile.position.x, projectile.position.y)
    }
    // Render chests
    chests.forEach { chest ->
        batch.draw(chest.texture, chest.position.x, chest.position.y)
    }
    batch.end()
}

//--------------------------------------------------------
    override fun dispose() { // Entfernt die Animationen
        batch.dispose()
        img.dispose()
        enemyTexture1.dispose()
        chestTexture.dispose()
    }

    fun openMenu () {
        println("Menü würde sich öffnen.")
    }

    fun createSampleLootTable(): LootTable {
        val commonItem = Item(name = "Common Item", type = ItemType.WEAPON, rarity = ItemRarity.COMMON, statBoosts = mapOf(), effects = listOf())
        val uncommonItem = Item(name = "Uncommon Item", type = ItemType.WEAPON, rarity = ItemRarity.UNCOMMON, statBoosts = mapOf(), effects = listOf())
        val rareItem = Item(name = "Rare Item", type = ItemType.ARMOR, rarity = ItemRarity.RARE, statBoosts = mapOf(), effects = listOf())
        val epicItem = Item(name = "Epic Item", type = ItemType.ARMOR, rarity = ItemRarity.EPIC, statBoosts = mapOf(), effects = listOf())
        val legendaryItem = Item(name = "Legendary Item", type = ItemType.ACCESSORY, rarity = ItemRarity.LEGENDARY, statBoosts = mapOf(), effects = listOf())

        val lootEntries = listOf(
                LootEntry(commonItem, weight = 82.0),
                LootEntry(uncommonItem, weight = 15.0),
                LootEntry(rareItem, weight = 2.5),
                LootEntry(epicItem, weight = 0.49),
                LootEntry(legendaryItem, weight = 0.01)
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

    fun getMouseWorldPosition(): Vector2 {
        val mousePos = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
        camera.unproject(mousePos)
        return Vector2(mousePos.x, mousePos.y)
    }
}