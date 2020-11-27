package com.sa.game.statemachines;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionEntity;
import com.sa.game.entities.Enemy;
import com.sa.game.entities.Enemy.XDirection;
import com.sa.game.entities.Player;
import com.sa.game.entities.PlayerStunProjectile;

public enum ClownEnemyBrain implements State<Enemy> {
    IS_SHOOT() {
        @Override
        public void update(Enemy enemy) {
            enemy.isStunned = true;
            enemy.idle();
            for(CollisionEntity collisionEntity : enemy.collisionEntity.collidees) {
                if(collisionEntity.userData instanceof Player) {
                    //enemy.stateMachine.changeState(IS_WEAPON);
                }
            }

            enemy.stunnedTime-=enemy.dt;
            if(enemy.stunnedTime <= 0f) {
                enemy.stunnedTime = 5f;
                enemy.stateMachine.revertToPreviousState();
            }
        }

        @Override
         public void exit(Enemy enemy) {
            enemy.isStunned = false;
        }
    },
    RESTING() {
        @Override
        public void update(Enemy enemy) {
            for(CollisionEntity collisionEntity : enemy.collisionEntity.collidees) {
                if(collisionEntity.userData instanceof PlayerStunProjectile) {
                    enemy.stateMachine.changeState(IS_SHOOT);
                }
            }
            enemy.currentDirection = XDirection.Idle;
            enemy.restTime -= enemy.dt;
            enemy.restTime = Math.max(enemy.restTime, 0f);
            if(enemy.restTime <= 0f) {
                enemy.restTime = MathUtils.random(3f, 5f);
                enemy.currentDirection = XDirection.Left;
                enemy.stateMachine.changeState(WANDERS_ON_PLATFORM);
            }
        }
    },
    WANDERS_ON_PLATFORM {
        @Override
        public void update(Enemy enemy) {
            for(CollisionEntity collisionEntity : enemy.collisionEntity.collidees) {
                if(collisionEntity.userData instanceof PlayerStunProjectile) {
                    enemy.stateMachine.changeState(IS_SHOOT);
                }
            }
            if(enemy.wallCollision) {
                if(enemy.currentDirection == Enemy.XDirection.Left)
                    enemy.currentDirection = Enemy.XDirection.Right;
                else
                    enemy.currentDirection = Enemy.XDirection.Left;
            }
            int tilex = (int) (enemy.collisionEntity.box.x + enemy.collisionEntity.box.width) / enemy.staticEnvironment.tileSizeInPixels;

            int tiley = (int) enemy.collisionEntity.box.y / enemy.staticEnvironment.tileSizeInPixels - 1;

            int id = 0;
            if(tilex >= 0 && tiley >= 0)
                id = enemy.staticEnvironment.getTileId(StaticEnvironment.LayerId.Floor, tilex, tiley);
            if(id == 0) {
                enemy.currentDirection = Enemy.XDirection.Left;
            }

            id = enemy.staticEnvironment.getTileId(StaticEnvironment.LayerId.Floor,
                                                             (int)(enemy.collisionEntity.box.x)/enemy.staticEnvironment.tileSizeInPixels,
                                                             (int)enemy.collisionEntity.box.y/enemy.staticEnvironment.tileSizeInPixels - 1);
            if(id == 0) {
                enemy.currentDirection = Enemy.XDirection.Right;
            }

            if(enemy.currentDirection == Enemy.XDirection.Left)
                enemy.moveLeft();
            if(enemy.currentDirection == Enemy.XDirection.Right)
                enemy.moveRight();
        }
    };

    ClownEnemyBrain() {
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
