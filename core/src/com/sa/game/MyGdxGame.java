package com.sa.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.sa.game.screens.GameScreen;
import com.sa.game.screens.TitleScreen;

public class MyGdxGame extends Game /*implements ApplicationListener*/ {
    public DeviceType deviceType;

    public MyGdxGame(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public void create() {
        Controller controllerA = null;
        Controller controllerB = null;

        for (Controller controller : Controllers.getControllers()) {
            Gdx.app.log("TAG", controller.getName());
            if(controllerA == null)
                controllerA = controller;
            if(controllerB == null)
                controllerB = controller;

        }
        setScreen(new TitleScreen(this, controllerA, controllerB));
    }

    public static MyGdxGame createDesktop() {
        final MyGdxGame game = new MyGdxGame(DeviceType.Desktop);
        return game;
    }

    public static MyGdxGame createMobile() {
        final MyGdxGame game = new MyGdxGame(DeviceType.Mobile);
        return game;
    }

}
