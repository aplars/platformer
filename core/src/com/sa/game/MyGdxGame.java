package com.sa.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.sa.game.screens.GameScreen;
import com.sa.game.screens.ScreenConstants;
import com.sa.game.screens.TitleScreen;
import com.sa.game.systems.control.KeyboardMapping;

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
        KeyboardMapping keyboardMapping = new KeyboardMapping();

        Preferences preferences = Gdx.app.getPreferences(ScreenConstants.PreferencesName);
        if(preferences.contains("KeyLeft"))
            keyboardMapping.Left = preferences.getInteger("KeyLeft");
        if(preferences.contains("KeyRight"))
            keyboardMapping.Right = preferences.getInteger("KeyRight");
        if(preferences.contains("KeyJump"))
            keyboardMapping.Jump = preferences.getInteger("KeyJump");
        if(preferences.contains("KeyFire"))
            keyboardMapping.Fire = preferences.getInteger("KeyFire");
        if(preferences.contains("KeyStart"))
            keyboardMapping.Start = preferences.getInteger("KeyStart");
        if(preferences.contains("KeyUp"))
            keyboardMapping.Up = preferences.getInteger("KeyUp");
        if(preferences.contains("KeyDown"))
            keyboardMapping.Down = preferences.getInteger("KeyDown");

        setScreen(new TitleScreen(this, new AssetManager(), keyboardMapping, controllerA, controllerB));
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
