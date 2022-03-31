package com.sa.game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.sa.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
      TexturePacker.process("art/sprites/", "enteties/", "game");
      Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
      config.setWindowedMode(1280, 720);
      config.useVsync(true);
      config.setIdleFPS(60);
      Lwjgl3Application app = new Lwjgl3Application(MyGdxGame.createDesktop(), config);
      app.exit();
      System.exit(0);
  }
}
