package com.sa.game.statemachines;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.sa.game.StaticEnvironment;
import com.sa.game.entities.Enemy;

public enum EnemyState implements State<Enemy>, GameStateCommon {

    RESTING() {
        @Override
        public void update(Enemy enemy) {
            restTime -= dt;
            restTime = Math.max(restTime, 0f);
            if(restTime <= 0f) {
                enemy.stateMachine.defaultStateMachine.changeState(WANDERS_ON_PLATFORM);
                restTime = MathUtils.random(3f, 5f);
            }
        }
    },
    WANDERS_ON_PLATFORM {
        @Override
        public void update(Enemy enemy) {
            if(wallCollision) {
                if(currentDirection == XDirection.Left)
                    currentDirection = XDirection.Right;
                else
                    currentDirection = XDirection.Left;
            }

            int id = staticEnvironment.getTileId(0, (int)(enemy.collisionRectangle.x+enemy.collisionRectangle.width)/staticEnvironment.tileSizeInPixels, (int)enemy.collisionRectangle.y/staticEnvironment.tileSizeInPixels - 1);
            if(id == 0) {
                currentDirection = XDirection.Left;
            }
            if(currentDirection == XDirection.Left)
                enemy.moveLeft(dt);
            if(currentDirection == XDirection.Right)
                enemy.moveRight(dt);
        }
    };
    enum XDirection{
        Left,
        Right;
    };

    float dt;
    float restTime = 0f;
    XDirection currentDirection = XDirection.Left;
    boolean groundCollision = false;
    boolean wallCollision = false;
    StaticEnvironment staticEnvironment;

    EnemyState() {
        restTime = MathUtils.random(3f, 5f);
    }
    @Override
    public void enter(Enemy enemy) {
    }

    @Override
    public void exit(Enemy enemy) {
    }

    @Override
    public boolean onMessage(Enemy troll, Telegram telegram) {
        return false;
    }

    @Override
    public void setDt(float dt) { this.dt = dt; }

    @Override
    public void setGroundCollision(boolean groundCollision) {
        this.groundCollision = groundCollision;
    }

    @Override
    public void setWallCollision(boolean wallCollision) {
        this.wallCollision = wallCollision;
    }

    @Override
    public void setStaticEnvironment(StaticEnvironment staticEnvironment) {
        this.staticEnvironment = staticEnvironment;
    }

}
