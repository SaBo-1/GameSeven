package com.mygdx.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle

class Enemy(
        texture: Texture,
        frameCols: Int,
        frameRows: Int,
        animationDuration: Float,
        val level: Int,
        var moveSpeed: Float,
        val lootTable: LootTable,
) {
    val animation: Animation<TextureRegion>

    val collisionBox = Rectangle()

    //        Level Berechnung
    var hitPoints: Int = level * 20 // 20 Lebenspunkte pro Level
    var attack: Int = level * 2 // 2 Angriffspunkte pro Level
    private var defense: Int = level * 1 // 1 Verteidigungspunkt pro Level

    init {
        val tmp = TextureRegion.split(texture, texture.width / frameCols, texture.height / frameRows)
        val frames = tmp.flatten()
        animation = Animation(animationDuration, *frames.toTypedArray())

        moveSpeed = level * 1.05f // 1 Bewegungsgeschwindigkeit pro Level
    }

    fun takeDamage(damage: Int) {
        val actualDamage = damage - defense
        hitPoints -= if (actualDamage > 0) actualDamage else 0
        if (hitPoints < 0) {
            hitPoints = 0
        }
    }

    fun isDead(): Boolean {
        return hitPoints <= 0
    }

    fun dropItem(): Item? {
        return if (isDead()) {
            lootTable.roll()
        } else {
            null
        }
    }
}
