package com.sa.game.components;

import com.badlogic.ashley.core.Component;

/**
 * Used to control an entity with a joystick interface.
 * Can be used by players and npc's.
 *
 */
public class ControlComponent implements Component {
    public boolean buttonA = false;
    public boolean buttonB = false;

    public boolean buttonLeft = false;
    public boolean buttonRight = false;
    public boolean buttonUp = false;
    public boolean buttonDown = false;

    public boolean buttonSelect = false;
    public boolean buttonStart = false;

    public void clear() {
        buttonA = false;
        buttonB = false;
        buttonLeft = false;
        buttonRight = false;
        buttonUp = false;
        buttonDown = false;
        buttonSelect = false;
        buttonStart = false;
    }
}
