package com.sa.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.ControlComponent;
import com.sa.game.components.DelayControlComponent;

public class DelayControlSystem extends IteratingSystem{

	public DelayControlSystem() {
      super(Family.all(DelayControlComponent.class, ControlComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
        DelayControlComponent delayAIComponent = ComponentMappers.delayAi.get(entity);
        ControlComponent controlComponent = ComponentMappers.control.get(entity);
        
        if(delayAIComponent.haveChanged) {
            if((delayAIComponent.mask & DelayControlComponent.BUTTONA) != 0)
                delayAIComponent.buttonA = controlComponent.buttonA;
            if((delayAIComponent.mask & DelayControlComponent.BUTTONB) != 0)
                delayAIComponent.buttonB = controlComponent.buttonB;
            if((delayAIComponent.mask & DelayControlComponent.BUTTONLEFT) != 0)
                delayAIComponent.buttonLeft = controlComponent.buttonLeft;
            if((delayAIComponent.mask & DelayControlComponent.BUTTONRIGHT) != 0)
                delayAIComponent.buttonRight = controlComponent.buttonRight;
            if((delayAIComponent.mask & DelayControlComponent.BUTTONDOWN) != 0)
                delayAIComponent.buttonDown = controlComponent.buttonDown;
            if((delayAIComponent.mask & DelayControlComponent.BUTTONUP) != 0)
                delayAIComponent.buttonUp = controlComponent.buttonUp;
            if((delayAIComponent.mask & DelayControlComponent.BUTTONSELECT) != 0)
                delayAIComponent.buttonSelect = controlComponent.buttonSelect;
            if((delayAIComponent.mask & DelayControlComponent.BUTTONSTART) != 0)
                delayAIComponent.buttonStart = controlComponent.buttonStart;
            delayAIComponent.haveChanged = false;
        }
        
        if (delayAIComponent.delay <= 0f) {
            entity.remove(DelayControlComponent.class);
            if((delayAIComponent.mask & DelayControlComponent.BUTTONA) != 0)
                controlComponent.buttonA = delayAIComponent.buttonA;
            if((delayAIComponent.mask & DelayControlComponent.BUTTONB) != 0)
                controlComponent.buttonB = delayAIComponent.buttonB;
            if((delayAIComponent.mask & DelayControlComponent.BUTTONLEFT) != 0)
                controlComponent.buttonLeft = delayAIComponent.buttonLeft;
            if((delayAIComponent.mask & DelayControlComponent.BUTTONRIGHT) != 0)
                controlComponent.buttonRight = delayAIComponent.buttonRight;
            if((delayAIComponent.mask & DelayControlComponent.BUTTONDOWN) != 0)
                controlComponent.buttonDown = delayAIComponent.buttonDown;
            if((delayAIComponent.mask & DelayControlComponent.BUTTONUP) != 0)
                controlComponent.buttonUp = delayAIComponent.buttonUp;
            if((delayAIComponent.mask & DelayControlComponent.BUTTONSELECT) != 0)
                controlComponent.buttonSelect = delayAIComponent.buttonSelect;
            if((delayAIComponent.mask & DelayControlComponent.BUTTONSTART) != 0)
            controlComponent.buttonStart = delayAIComponent.buttonStart;
        }
        else {
            delayAIComponent.delay -= deltaTime;
            if((delayAIComponent.mask & DelayControlComponent.BUTTONA) != 0)
                controlComponent.buttonA = false;
            if((delayAIComponent.mask & DelayControlComponent.BUTTONB) != 0)
                controlComponent.buttonB = false;
            if((delayAIComponent.mask & DelayControlComponent.BUTTONLEFT) != 0)
                controlComponent.buttonLeft = false;
            if((delayAIComponent.mask & DelayControlComponent.BUTTONRIGHT) != 0)
                controlComponent.buttonRight = false;
            if((delayAIComponent.mask & DelayControlComponent.BUTTONDOWN) != 0)
                controlComponent.buttonDown = false;
            if((delayAIComponent.mask & DelayControlComponent.BUTTONUP) != 0)
                controlComponent.buttonUp = false;
            if((delayAIComponent.mask & DelayControlComponent.BUTTONSELECT) != 0)
                controlComponent.buttonSelect = false;
            if((delayAIComponent.mask & DelayControlComponent.BUTTONSTART) != 0)
                controlComponent.buttonStart = false;
        }
	}
}
