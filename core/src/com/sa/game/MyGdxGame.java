package com.sa.game;

import java.util.concurrent.atomic.AtomicBoolean;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.PerformanceCounters;
import com.badlogic.gdx.utils.TimeUtils;
import com.sa.game.editor.Editor;
import com.sa.game.models.EditorModel;
import com.sa.game.screens.GameScreen;

public class MyGdxGame extends Game implements ApplicationListener {
    @Override
    public void create() {
        setScreen(new GameScreen());
    }

    public static MyGdxGame createDesktop() {
        final MyGdxGame game = new MyGdxGame();
        return game;
    }
}
