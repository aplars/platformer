package com.sa.game;

import com.badlogic.gdx.Game;
import com.sa.game.screens.GameScreen;

public class MyGdxGame extends Game /*implements ApplicationListener*/ {
    @Override
    public void create() {
        setScreen(new GameScreen());
    }

    public static MyGdxGame createDesktop() {
        final MyGdxGame game = new MyGdxGame();
        return game;
    }
}
