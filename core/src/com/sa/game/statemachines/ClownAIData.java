package com.sa.game.statemachines;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.EntityControlComponent;

public class ClownAIData {

    public class Constants {
        public final float jumpImpulse;
        public final float moveForce;

        public Constants(float jumpImpulse, float moveForce) {
            this.jumpImpulse = jumpImpulse;
            this.moveForce = moveForce;
        }
    }

    public class OutData {
        public float moveForce;
    }

    public  class InData {
        public float dt;
        public float time;
        public CollisionEntity collisionEntity;
    }

    public class CountDown {
        float t;
        public void act(float dt) {
            t-=dt;
        }

        public void  setTime(float time) {
            t = time;
        }

        public boolean isComplete() {
            return t <= 0f;
        }
    }

    final Constants constants;

    public  InData input = new InData();

    public OutData output = new OutData();

    public CountDown countDown = new CountDown();

    public DefaultStateMachine<ClownAIData, ClownAIState> stateMachine;

    public ClownAIData(float moveForce, CollisionEntity collisionEntity) {
        this.stateMachine = new DefaultStateMachine<>(this, ClownAIState.START);
        this.constants = new Constants(0, moveForce);
        this.input.collisionEntity = collisionEntity;
    }
}
