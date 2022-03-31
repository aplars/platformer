package com.sa.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.sa.game.screens.ScreenConstants;
import com.sa.game.screens.TitleScreen;
import com.sa.game.systems.control.ControllerMapping;
import com.sa.game.systems.control.KeyboardMapping;

public class MyGdxGame extends Game {
    public DeviceType deviceType;

    public MyGdxGame(final DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public void create() {
        Controller controllerA = null;
        Controller controllerB = null;


        for (final Controller controller : Controllers.getControllers()) {
            Gdx.app.log("TAG", controller.getName());
            if(controllerA == null)
                controllerA = controller;
            if(controllerB == null)
                controllerB = controller;
        }
        final KeyboardMapping keyboardMapping = new KeyboardMapping();

        final Preferences preferences = Gdx.app.getPreferences(ScreenConstants.PreferencesName);
        if(preferences.contains("KeyLeft"))
            keyboardMapping.Left = preferences.getInteger("KeyLeft");
        if(preferences.contains("KeyRight"))
            keyboardMapping.Right = preferences.getInteger("KeyRight");
        if(preferences.contains("KeyA"))
            keyboardMapping.A = preferences.getInteger("KeyA");
        if(preferences.contains("KeyB"))
            keyboardMapping.B = preferences.getInteger("KeyB");
        if(preferences.contains("KeyStart"))
            keyboardMapping.Start = preferences.getInteger("KeyStart");
        if(preferences.contains("KeyUp"))
            keyboardMapping.Up = preferences.getInteger("KeyUp");
        if(preferences.contains("KeyDown"))
            keyboardMapping.Down = preferences.getInteger("KeyDown");

        ControllerMapping controllerMappingA = null;
        if(controllerA != null) {
            controllerMappingA = new ControllerMapping(controllerA);
            if (preferences.contains("ControllerALeft"))
                controllerMappingA.Left = preferences.getInteger("ControllerALeft");
            if (preferences.contains("ControllerARight"))
                controllerMappingA.Right = preferences.getInteger("ControllerARight");
            if (preferences.contains("ControllerAA"))
                controllerMappingA.A = preferences.getInteger("ControllerAA");
            if (preferences.contains("ControllerAB"))
                controllerMappingA.B = preferences.getInteger("ControllerAB");
            if (preferences.contains("ControllerAStart"))
                controllerMappingA.Start = preferences.getInteger("ControllerAStart");
            if (preferences.contains("ControllerAUp"))
                controllerMappingA.Up = preferences.getInteger("ControllerAUp");
            if (preferences.contains("ControllerADown"))
                controllerMappingA.Down = preferences.getInteger("ControllerADown");
        }

        ControllerMapping controllerMappingB = null;
        if(controllerB != null)
            controllerMappingB = new ControllerMapping(controllerB);

        setScreen(new TitleScreen(this, new AssetManager(), keyboardMapping, controllerA, controllerMappingA, controllerB, controllerMappingB));
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
