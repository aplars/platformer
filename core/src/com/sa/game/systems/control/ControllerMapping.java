package com.sa.game.systems.control;

import com.badlogic.gdx.controllers.Controller;

public class ControllerMapping {
    public int Left;
    public int Right;
    public int A;
    public int B;
    public int Start;
    public int Up;
    public int Down;

    public ControllerMapping(Controller controller) {
        Left = controller.getMapping().buttonDpadLeft;
        Right = controller.getMapping().buttonDpadRight;
        A = controller.getMapping().buttonA;
        B = controller.getMapping().buttonB;
        Start = controller.getMapping().buttonStart;
        Up = controller.getMapping().buttonDpadUp;
        Down = controller.getMapping().buttonDpadDown;;
    }

    public void set(ControllerMapping other) {
        this.Left = other.Left;
        this.Right = other.Right;
        this.A = other.A;
        this.B = other.B;
        this.Start = other.Start;
        this.Up = other.Up;
        this.Down = other.Down;
    }
}
