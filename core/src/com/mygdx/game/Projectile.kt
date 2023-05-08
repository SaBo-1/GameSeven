package com.mygdx.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class Projectile(
    texture: Texture,
    frameCols: Int,
    frameRows: Int,
    animationDuration: Float,
    val position: Vector2,
    val velocity: Vector2
) {
    val animation: Animation<TextureRegion>
    val collisionBox = Rectangle()

    init {
        val tmp = TextureRegion.split(texture, texture.width / frameCols, texture.height / frameRows)
        val frames = tmp.flatten()
        animation = Animation(animationDuration, *frames.toTypedArray())
    }

    fun update(deltaTime: Float) {
        position.x += velocity.x * deltaTime
        position.y += velocity.y * deltaTime
        collisionBox.set(position.x, position.y, animation.getKeyFrame(0f).regionWidth.toFloat(), animation.getKeyFrame(0f).regionHeight.toFloat())
    }
}