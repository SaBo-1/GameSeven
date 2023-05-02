package com.mygdx.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle

class Player(texture: Texture, frameCols: Int, frameRows: Int, animationDuration: Float) {
    var animation: Animation<TextureRegion>

    val collisionBox = Rectangle()

    init {
        val tmp = TextureRegion.split(texture, texture.width / frameCols, texture.height / frameRows)
        val frames = tmp.flatten()
        animation = Animation(animationDuration, *frames.toTypedArray())
    }

}