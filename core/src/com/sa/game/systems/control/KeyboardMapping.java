package com.sa.game.systems.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.sa.game.screens.ScreenConstants;

public class KeyboardMapping {
    public int Left = Input.Keys.A;
    public int Right = Input.Keys.D;
    public int A =  Input.Keys.J;
    public int B =  Input.Keys.K;
    public int Start = Input.Keys.SPACE;
    public int Up = Input.Keys.W;
    public int Down = Input.Keys.S;

    public KeyboardMapping() {
    }

    public void set(KeyboardMapping other) {
        this.Left = other.Left;
        this.Right = other.Right;
        this.A = other.A;
        this.B = other.B;
        this.Start = other.Start;
        this.Up = other.Up;
        this.Down = other.Down;
    }
}
