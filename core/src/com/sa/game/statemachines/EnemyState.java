package com.sa.game.statemachines;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.entities.Enemy;
import com.sa.game.entities.Player;
import com.sa.game.entities.PlayerProjectile;
import com.sa.game.entities.Enemy.XDirection;

public enum EnemyState implements State<Enemy> {
    IS_SHOOT() {
        @Override
        public void update(Enemy enemy) {
            enemy.stateData.isStunned = true;
            enemy.idle(enemy.stateData.dt);
            for(CollisionEntity collisionEntity : enemy.collisionEntity.collidees) {
                if(collisionEntity.userData instanceof Player) {
                    //enemy.stateMachine.changeState(IS_WEAPON);
                }
            }

            enemy.stateData.stunnedTime-=enemy.stateData.dt;
            if(enemy.stateData.stunnedTime <= 0f) {
                enemy.stateData.stunnedTime = 5f;
                enemy.stateMachine.revertToPreviousState();
            }
        }

        @Override
        public void exit(Enemy enemy) {
            enemy.stateData.isStunned = false;
        }
    },
    RESTING() {
        @Override
        public void update(Enemy enemy) {
            for(CollisionEntity collisionEntity : enemy.collisionEntity.collidees) {
                if(collisionEntity.userData instanceof PlayerProjectile) {
                    enemy.stateMachine.changeState(IS_SHOOT);
                }
            }
            enemy.stateData.currentDirection = XDirection.Idle;
            enemy.stateData.restTime -= enemy.stateData.dt;
            enemy.stateData.restTime = Math.max(enemy.stateData.restTime, 0f);
            if(enemy.stateData.restTime <= 0f) {
                enemy.stateData.restTime = MathUtils.random(3f, 5f);
                enemy.stateData.currentDirection = XDirection.Left;
                enemy.stateMachine.changeState(WANDERS_ON_PLATFORM);
            }
        }
    },
    WANDERS_ON_PLATFORM {
        @Override
        public void update(Enemy enemy) {
            for(CollisionEntity collisionEntity : enemy.collisionEntity.collidees) {
                if(collisionEntity.userData instanceof PlayerProjectile) {
                    enemy.stateMachine.changeState(IS_SHOOT);
                }
            }
            if(enemy.stateData.wallCollision) {
                if(enemy.stateData.currentDirection == Enemy.XDirection.Left)
                    enemy.stateData.currentDirection = Enemy.XDirection.Right;
                else
                    enemy.stateData.currentDirection = Enemy.XDirection.Left;
            }
            int tilex = (int) (enemy.collisionEntity.box.x + enemy.collisionEntity.box.width)
                    / enemy.stateData.staticEnvironment.tileSizeInPixels;

            int tiley = (int) enemy.collisionEntity.box.y / enemy.stateData.staticEnvironment.tileSizeInPixels - 1;

            int id = 0;
            if(tilex >= 0 && tiley >= 0)
                id = enemy.stateData.staticEnvironment.getTileId(StaticEnvironment.TileId.Floor, tilex, tiley);
            if(id == 0) {
                enemy.stateData.currentDirection = Enemy.XDirection.Left;
            }

            id = enemy.stateData.staticEnvironment.getTileId(StaticEnvironment.TileId.Floor,
                                                             (int)(enemy.collisionEntity.box.x)/enemy.stateData.staticEnvironment.tileSizeInPixels,
                                                             (int)enemy.collisionEntity.box.y/enemy.stateData.staticEnvironment.tileSizeInPixels - 1);
            if(id == 0) {
                enemy.stateData.currentDirection = Enemy.XDirection.Right;
            }

            if(enemy.stateData.currentDirection == Enemy.XDirection.Left)
                enemy.moveLeft(enemy.stateData.dt);
            if(enemy.stateData.currentDirection == Enemy.XDirection.Right)
                enemy.moveRight(enemy.stateData.dt);
        }
    };

    EnemyState() {
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
}
