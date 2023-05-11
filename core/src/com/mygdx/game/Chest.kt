package com.mygdx.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2


class Chest(val texture: Texture, val position: Vector2, val item: Item) {
    val collisionBox = Rectangle(position.x, position.y, texture.width.toFloat(), texture.height.toFloat())
}