package com.sa.game.systems.control;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.sa.game.components.ControlComponent;
import com.sa.game.components.Player1Component;

public class PlayerInputSystem extends IteratingSystem{
    private final ComponentMapper<ControlComponent> controlMap = ComponentMapper.getFor(ControlComponent.class);

    public PlayerInputSystem() {
        super(Family.all(ControlComponent.class, Player1Component.class).get());

    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        final ControlComponent controlComponent = controlMap.get(entity);
        controlComponent.clear();

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            controlComponent.buttonLeft = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            controlComponent.buttonRight = true;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            controlComponent.buttonUp = true;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            controlComponent.buttonDown = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            controlComponent.buttonA = true;
        }
        if(!Gdx.input.isKeyPressed(Input.Keys.K)) {
            controlComponent.buttonBTime = Math.min(0.1f, controlComponent.buttonBTime);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.K) && controlComponent.buttonBTime <= 0f) {
            controlComponent.buttonB = true;
            controlComponent.buttonBTime = 0.5f;
        }
        controlComponent.buttonBTime = Math.max(0f, controlComponent.buttonBTime - deltaTime);
    }
}
