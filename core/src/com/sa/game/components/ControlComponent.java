package com.sa.game.components;

import com.badlogic.ashley.core.Component;

public class ControlComponent implements Component {
    public boolean buttonA = false;
    public boolean buttonB = false;

    public boolean buttonLeft = false;
    public boolean buttonRight = false;
    public boolean buttonUp = false;
    public boolean buttonDown = false;

    public void clear() {
        buttonA = false;
        buttonB = false;
        buttonLeft = false;
        buttonRight = false;
        buttonUp = false;
        buttonDown = false;
    }
}
