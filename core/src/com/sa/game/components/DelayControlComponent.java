package com.sa.game.components;

import com.badlogic.ashley.core.Component;

/**
 * Delays all controls for a specified amount of time.
 * Can be used to delay the game when a new level has been loaded.
 * Another use may be to delay an enemy when it gets hit with something.
 */
public class DelayControlComponent implements Component{
    public float delay = 0f; //Delay in seconds 
    public boolean buttonA = false;
    public boolean buttonB = false;

    public boolean buttonLeft = false;
    public boolean buttonRight = false;
    public boolean buttonUp = false;
    public boolean buttonDown = false;
    public boolean buttonSelect = false;
    public boolean buttonStart = false;

    public boolean haveChanged = false;

    public static short BUTTONA = 1;
    public static short BUTTONB = 2;
    public static short BUTTONLEFT = 4;
    public static short BUTTONRIGHT = 8;
    public static short BUTTONUP = 16;
    public static short BUTTONDOWN = 32;
    public static short BUTTONSELECT = 64;
    public static short BUTTONSTART = 128;

    public short mask = Short.MAX_VALUE;

    public DelayControlComponent(float delay) {
        this.delay = delay;
        haveChanged = true;
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
        this.haveChanged = true;
    }
}
