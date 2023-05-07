package com.mygdx.game

import com.badlogic.gdx.Screen
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.utils.viewport.ScreenViewport

class MainMenuScreen(private val game: GameSevenMain) : Screen {
    private val stage: Stage = Stage(ScreenViewport())
    private val skin: Skin = Skin(Gdx.files.internal("uiskin.json"))
    private val playButton: TextButton
    private val settingsButton: TextButton

    init {
        Gdx.input.inputProcessor = stage

        playButton = TextButton("Start Game", skin, "default")
        playButton.width = 200f
        playButton.height = 50f
        playButton.setPosition((Gdx.graphics.width - playButton.width) / 2, (Gdx.graphics.height - playButton.height) / 2 + 100)
        playButton.addListener {
            game.screen = game.gameScreen
            false
        }

        settingsButton = TextButton("Settings", skin, "default")
        settingsButton.width = 200f
        settingsButton.height = 50f
        settingsButton.setPosition((Gdx.graphics.width - settingsButton.width) / 2, (Gdx.graphics.height - settingsButton.height) / 2 - 100)
        settingsButton.addListener {
            // Hier können Sie den Code zum Anzeigen der Einstellungen hinzufügen
            false
        }

        stage.addActor(playButton)
        stage.addActor(settingsButton)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        stage.act(delta)
        stage.draw()
    }

    // Implementieren Sie die restlichen Methoden von Screen (show, resize, pause, resume, hide, dispose), falls benötigt
}
