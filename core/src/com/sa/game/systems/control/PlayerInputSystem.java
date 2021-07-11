package com.sa.game.systems.control;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.components.ControlComponent;
import com.sa.game.components.DelayControlComponent;
import com.sa.game.components.Player1Component;

/**
 * Reads from the keyboard and joystick and writes to the control component.
 *
 */
public class PlayerInputSystem extends IteratingSystem {
    public class MyGestureListener implements GestureDetector.GestureListener {

        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            int w = Gdx.graphics.getWidth();
            int h = Gdx.graphics.getHeight();


            if(x < w/2 && y < h/2)
                buttonB = true;
            else if(x < w/2 && y > h/2)
                buttonA = true;
            else
                startX = x;

            return false;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {

            return false;
        }

        @Override
        public boolean longPress(float x, float y) {

            return false;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {

            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            left = false;
            right = false;

            int w = Gdx.graphics.getWidth();
            if(x > w/2 && (x-startX) > 2.5f) {
                right = true;
                left = false;
            }
            if(x > w/2 && (x-startX) < -2.5f) {
                left = true;
                right = false;
            }
            return false;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            left = false;
            right = false;

            return false;
        }

        @Override
        public boolean zoom (float originalDistance, float currentDistance){

            return false;
        }

        @Override
        public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer){

            return

                    false;
        }
        @Override
        public void pinchStop () {
        }
        float startX = 0;

        boolean left = false;
        boolean right = false;
        boolean buttonA = false;
        boolean buttonB = false;
    }

    private final ComponentMapper<ControlComponent> controlMap = ComponentMapper.getFor(ControlComponent.class);
    Controller controller;
    KeyboardMapping keyboardMapping;
    MyGestureListener myGestureListener;

    public PlayerInputSystem(Controller controller, KeyboardMapping keyboardMapping) {
        super(Family.all(ControlComponent.class, Player1Component.class).exclude(DelayControlComponent.class).get());
        this.controller = controller;
        this.keyboardMapping = keyboardMapping;
        myGestureListener = new MyGestureListener();
        Gdx.input.setInputProcessor(new GestureDetector(myGestureListener));
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        final ControlComponent controlComponent = controlMap.get(entity);
        controlComponent.clear();

        if(myGestureListener.right) {
            controlComponent.buttonRight = true;
        }
        if(myGestureListener.left) {
            controlComponent.buttonLeft = true;
        }
        if(myGestureListener.buttonA) {
            controlComponent.buttonA = true;
            myGestureListener.buttonA = false;
        }
        if(myGestureListener.buttonB) {
            controlComponent.buttonB = true;
            myGestureListener.buttonB = false;
        }
        if (Gdx.input.isKeyPressed(keyboardMapping.Left)) {
            controlComponent.buttonLeft = true;
        }
        if (Gdx.input.isKeyPressed(keyboardMapping.Right)) {
            controlComponent.buttonRight = true;
        }
        if (Gdx.input.isKeyPressed(keyboardMapping.Jump)) {
            controlComponent.buttonA = true;
        }
        if(Gdx.input.isKeyPressed(keyboardMapping.Fire) /*&& controlComponent.buttonBTime <= 0f*/) {
            controlComponent.buttonB = true;
        }
        if(this.controller != null) {
            if (this.controller.getButton(controller.getMapping().buttonDpadLeft)) {
                controlComponent.buttonLeft = true;
            }
            if (this.controller.getButton(controller.getMapping().buttonDpadRight)) {
                controlComponent.buttonRight = true;
            }
            if (this.controller.getButton(controller.getMapping().buttonA)) {
                controlComponent.buttonA = true;
            }
            if (this.controller.getButton(controller.getMapping().buttonB) /*&& controlComponent.buttonBTime <= 0f*/) {
                controlComponent.buttonB = true;
            }
            if(this.controller.getButton(controller.getMapping().buttonBack)) {
                controlComponent.buttonSelect = true;
            }
            if(this.controller.getButton(controller.getMapping().buttonStart)) {
                controlComponent.buttonStart = true;
            }
        }

        //boolean buttonPressed = controller.getButton(buttonCode);
        //if() {
            
        //}
        //controlComponent.buttonBTime = Math.max(0f, controlComponent.buttonBTime - deltaTime);
    }
}
