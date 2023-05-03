package com.mygdx.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle

class Player(texture: Texture, frameCols: Int, frameRows: Int, animationDuration: Float) {
    var animation: Animation<TextureRegion>

    val collisionBox = Rectangle()

    // Vorhandene Attribute
    var level: Int = 1
    var hitPoints: Int = 100
    var attack: Int = 10
    var defense: Int = 5
    var experience: Int = 0
    val maxLevel: Int = 100
    var initiative: Float = 1.0f
    var baseMoveSpeed: Float = 1f // Basisbewegungsgeschwindigkeit
    val moveSpeed: Float
        get() = baseMoveSpeed * initiative

    init {
        val tmp = TextureRegion.split(texture, texture.width / frameCols, texture.height / frameRows)
        val frames = tmp.flatten()
        animation = Animation(animationDuration, *frames.toTypedArray())
    }

    fun takeDamage(damage: Int) {
        val actualDamage = damage - defense
        hitPoints -= if (actualDamage > 0) actualDamage else 0
        if (hitPoints < 0) {
            hitPoints = 0
        }
    }

    fun gainExperience(exp: Int) {
        experience += exp
        while (experience >= experienceToNextLevel() && level < maxLevel) {
            levelUp()
        }
    }

    private fun levelUp() {
        level += 1
        hitPoints += 10 // Erhöht die Lebenspunkte um 10 pro Level
        attack += 2 // Erhöht den Angriffswert um 2 pro Level
        defense += 1 // Erhöht den Verteidigungswert um 1 pro Level
        initiative += 0.01f // Erhöht den Initiative um 0.01 pro Level
    }

    private fun experienceToNextLevel(): Int {
        return level * 100 // Erfahrungspunkte benötigt, um das nächste Level zu erreichen
    }
}
